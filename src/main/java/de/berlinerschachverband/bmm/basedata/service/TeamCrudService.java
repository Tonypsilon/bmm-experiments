package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.Iterables;
import de.berlinerschachverband.bmm.basedata.data.PlayerData;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.RemoveTeamsData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamCrudService {

    private final TeamDataAccessService teamDataAccessService;

    private final PlayerService playerService;

    public TeamCrudService(TeamDataAccessService teamDataAccessService,
                           PlayerService playerService) {
        this.teamDataAccessService = teamDataAccessService;
        this.playerService = playerService;
    }

    /**
     * Given the name of a club and a number, add this many teams for the club. If the current last team
     * contains more than 16 players, removes players from nr 17.
     *
     * @param createTeamsData
     */
    public void createTeams(CreateTeamsData createTeamsData) {
        List<TeamData> currentTeams = teamDataAccessService.getTeamsOfClub(createTeamsData.getClubName());
        if (!currentTeams.isEmpty()) {
            TeamData currentLastTeam = Iterables.getLast(currentTeams);
            List<PlayerData> playersOfFormerLastTeam = playerService.getAllPlayersOfTeam(currentLastTeam.id());
            for(PlayerData player : playersOfFormerLastTeam) {
                if (player.boardNumber() > 16) {
                    playerService.deletePlayer(player);
                }
            }
        }
        teamDataAccessService.createTeams(createTeamsData);
    }

    /**
     * Given a club and a number, removes this many teams for the club.
     *
     * @param removeTeamsData
     */
    public void removeTeams(RemoveTeamsData removeTeamsData) {
        List<TeamData> allTeamsOfClub = teamDataAccessService.getTeamsOfClub(removeTeamsData.getClubName());
        List<TeamData> teamsToDelete = allTeamsOfClub.subList(
                Math.max(0, allTeamsOfClub.size() - removeTeamsData.getNumberOfTeamsToDelete()),
                allTeamsOfClub.size());
        for(TeamData teamToDelete : teamsToDelete) {
            for(PlayerData playerToDelete: playerService.getAllPlayersOfTeam(teamToDelete.id())) {
                playerService.deletePlayer(playerToDelete);
            }
            teamDataAccessService.removeTeam(teamToDelete);
        }
    }

}
