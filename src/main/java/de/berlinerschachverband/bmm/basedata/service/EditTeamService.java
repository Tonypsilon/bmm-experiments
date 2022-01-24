package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.AvailablePlayer;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.basedata.data.PlayerData;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.AddPlayerData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PlayerAssignmentData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PlayerThymeleafData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PrepareEditTeamData;
import de.berlinerschachverband.bmm.exceptions.TeamNotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class EditTeamService {

    private final TeamDataAccessService teamDataAccessService;
    private final PlayerService playerService;
    private final AvailablePlayerService availablePlayerService;
    private final ClubService clubService;

    public EditTeamService(TeamDataAccessService teamDataAccessService,
                           PlayerService playerService,
                           AvailablePlayerService availablePlayerService,
                           ClubService clubService) {
        this.teamDataAccessService = teamDataAccessService;
        this.playerService = playerService;
        this.availablePlayerService = availablePlayerService;
        this.clubService = clubService;
    }

    @Transactional
    public PrepareEditTeamData getTeamForEditing(String clubName, Integer teamNumber) {
        TeamData teamData = teamDataAccessService.getTeamsOfClub(clubName).stream()
                .filter(teamData1 -> teamData1.number().equals(teamNumber))
                .findFirst().orElseThrow(() -> new TeamNotFoundException(clubName,teamNumber));
        List<PlayerData> playersOfTeam = playerService.getAllPlayersOfTeam(teamData.id());
        PrepareEditTeamData prepareEditTeamData = new PrepareEditTeamData();
        prepareEditTeamData.setMaxNumberOfPlayers(teamDataAccessService.isLastTeam(teamData.id()) ? 32 : 16);
        prepareEditTeamData.setNumberOfBoards(8);
        prepareEditTeamData.setClubName(clubName);
        prepareEditTeamData.setTeamNumber(teamNumber);
        List<AddPlayerData> defaultAvailablePlayer = new ArrayList<>();
        defaultAvailablePlayer.add(defaultAvailablePlayer());
        prepareEditTeamData.setAvailablePlayers(defaultAvailablePlayer);
        prepareEditTeamData.getAvailablePlayers().addAll(
                availablePlayerService.getAvailablePlayersByZps(clubService.getClub(clubName).getZps())
                .stream().map(this::toAddPlayerData).toList());
        prepareEditTeamData.setCurrentTeamPlayers(playersOfTeam.stream().map(this::toPlayerThymeleafData).toList());

        // Set the current board numbers
        for(PlayerData currentPlayer : playersOfTeam) {
            prepareEditTeamData.getAvailablePlayers().stream()
                    .filter(availablePlayer -> availablePlayer.getMemberNumber().equals(currentPlayer.memberNumber()))
                    .findFirst()
                    .ifPresent(availablePlayer -> availablePlayer.setCurrentBoardNumber(currentPlayer.boardNumber()));
        }

        return prepareEditTeamData;
    }

    @Transactional(rollbackFor = Exception.class)
    public void editTeam(PrepareEditTeamData prepareEditTeamData) {
        TeamData team = teamDataAccessService
                .getTeamsOfClub(prepareEditTeamData.getClubName())
                .stream()
                .filter(teamData -> teamData.number().equals(prepareEditTeamData.getTeamNumber()))
                .findFirst()
                .orElseThrow(() -> new TeamNotFoundException(
                        prepareEditTeamData.getClubName(),
                        prepareEditTeamData.getTeamNumber()));
        List<PlayerData> currentPlayersOfTeam = playerService.getAllPlayersOfTeam(team.id());
        for (PlayerData playerData : currentPlayersOfTeam) {
            playerService.deletePlayer(playerData);
        }
        Integer zps = clubService.getClub(prepareEditTeamData.getClubName()).getZps();
        Iterator<Integer> futureTeamIterator = prepareEditTeamData.getFutureTeamPlayersMemberNumbers().iterator();
        Integer boardNumber = 1;
        Integer memberNumber;
        while(futureTeamIterator.hasNext()) {
            memberNumber = futureTeamIterator.next();
            if(memberNumber < 0) {
                break;
            }
            playerService.assignPlayerToTeam(new PlayerAssignmentData(zps, memberNumber, boardNumber),
                    team);
            boardNumber++;
        }

    }

    private PlayerThymeleafData toPlayerThymeleafData(PlayerData playerData) {
        PlayerThymeleafData playerThymeleafData = new PlayerThymeleafData();
        playerThymeleafData.setName(playerData.fullName());
        playerThymeleafData.setMemberNumber(playerData.memberNumber());
        return playerThymeleafData;
    }

    private AddPlayerData toAddPlayerData(AvailablePlayerData availablePlayerData) {
        AddPlayerData addPlayerData = new AddPlayerData();
        addPlayerData.setZps(availablePlayerData.zps());
        addPlayerData.setMemberNumber(availablePlayerData.memberNumber());
        addPlayerData.setFullName(availablePlayerData.fullName());
        addPlayerData.setSurname(addPlayerData.getSurname());
        addPlayerData.setDwz(addPlayerData.getDwz().orElse(0));
        return addPlayerData;
    }

    private AddPlayerData defaultAvailablePlayer() {
        AddPlayerData player = new AddPlayerData();
        player.setZps(-1);
        player.setMemberNumber(-1);
        player.setFullName(" ");
        player.setSurname(" ");
        player.setCurrentBoardNumber( -1);
        return player;
    }
}
