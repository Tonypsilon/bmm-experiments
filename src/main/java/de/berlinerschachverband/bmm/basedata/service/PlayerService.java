package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Team;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.basedata.data.Player;
import de.berlinerschachverband.bmm.basedata.data.PlayerData;
import de.berlinerschachverband.bmm.basedata.data.PlayerRepository;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PlayerAssignmentData;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PlayerService {

    private final TeamService teamService;
    private final AvailablePlayerService availablePlayerService;
    private final PlayerRepository playerRepository;

    public PlayerService(TeamService teamService,
                         AvailablePlayerService availablePlayerService,
                         PlayerRepository playerRepository) {
        this.teamService = teamService;
        this.availablePlayerService = availablePlayerService;
        this.playerRepository = playerRepository;
    }

    public List<PlayerData> getAllPlayersOfTeam(Long teamId) {
        return playerRepository.findByTeam_Id(teamId)
                .stream()
                .map(this::toPlayerData)
                .sorted(Comparator.comparing(PlayerData::boardNumber))
                .toList();
    }

    public void assignPlayerToTeam(PlayerAssignmentData playerAssignmentData, TeamData teamData) {
        if(playerAssignmentData.boardNumber() < 1) {
            throw new BmmException("board boardNumber < 1");
        }
        if(playerAssignmentData.boardNumber() > 32) {
            throw new BmmException("board boardNumber > 32");
        }
        AvailablePlayerData availablePlayerData = availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(
                playerAssignmentData.zps(), playerAssignmentData.memberNumber());
        Team team = teamService.getTeamById(teamData.id());
        if(team.getDivision().isPresent()) {
            throw new BmmException("Player can not be assigned to team that already is in a division.");
        }
        if(Boolean.TRUE.equals(playerExistsByTeamIdAndNumber(teamData.id(), playerAssignmentData.boardNumber()))) {
            throw new BmmException("Player with that boardNumber is already on the team.");
        }
        if(playerAssignmentData.boardNumber() > 16 && Boolean.FALSE.equals(teamService.isLastTeam(teamData.id()))) {
            throw new BmmException("Only last team of club can have more than 16 members.");
        }
        if(!playerAssignmentData.zps().equals(teamData.clubData().zps())) {
            throw new BmmException("The player is not a member of the club.");
        }
        Player player = new Player();
        player.setFullName(availablePlayerData.fullName());
        player.setSurname(availablePlayerData.surname());
        player.setTeam(team);
        player.setBoardNumber(playerAssignmentData.boardNumber());
        player.setDwz(availablePlayerData.dwz().orElse(null));
        player.setElo(availablePlayerData.elo().orElse(null));
        player.setTitle(availablePlayerData.title().orElse(null));
        player.setZps(availablePlayerData.zps());
        player.setMemberNumber(availablePlayerData.memberNumber());
        playerRepository.saveAndFlush(player);
        availablePlayerService.deleteAvailablePlayer(playerAssignmentData.zps(), playerAssignmentData.memberNumber());
    }

    public Boolean playerExistsByTeamIdAndNumber(Long teamId, Integer number) {
        return playerRepository.findByTeam_IdAndBoardNumber(teamId, number).isPresent();
    }

    public PlayerData toPlayerData(Player player) {
        return new PlayerData(player.getId(),
                player.getFullName(),
                player.getSurname(),
                player.getFideId(),
                teamService.toTeamData(player.getTeam()),
                player.getBoardNumber(),
                player.getDwz(),
                player.getElo(),
                player.getTitle(),
                player.getZps(),
                player.getMemberNumber()
        );
    }

}
