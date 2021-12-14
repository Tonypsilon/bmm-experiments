package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.PlayerData;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.EditTeamData;
import de.berlinerschachverband.bmm.exceptions.TeamNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditTeamService {

    private final TeamService teamService;
    private final PlayerService playerService;

    public EditTeamService(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    public EditTeamData getTeamForEditing(String clubName, Integer teamNumber) {
        TeamData teamData = teamService.getTeamsOfClub(clubName).stream()
                .filter(teamData1 -> teamData1.number().equals(teamNumber))
                .findFirst().orElseThrow(() -> new TeamNotFoundException(clubName,teamNumber));
        List<PlayerData> playersOfTeam = playerService.getAllPlayersOfTeam(teamData.id());
        EditTeamData editTeamData = new EditTeamData();
        editTeamData.setNumberOfBoards(8);
        editTeamData.setClubName(clubName);
        editTeamData.setTeamNumber(teamNumber);
        return editTeamData;
    }
}
