package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.basedata.data.PlayerData;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.AddPlayerData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.EditTeamData;
import de.berlinerschachverband.bmm.exceptions.TeamNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditTeamService {

    private final TeamDataAccessService teamDataAccessService;
    private final PlayerService playerService;

    public EditTeamService(TeamDataAccessService teamDataAccessService, PlayerService playerService) {
        this.teamDataAccessService = teamDataAccessService;
        this.playerService = playerService;
    }

    public EditTeamData getTeamForEditing(String clubName, Integer teamNumber) {
        TeamData teamData = teamDataAccessService.getTeamsOfClub(clubName).stream()
                .filter(teamData1 -> teamData1.number().equals(teamNumber))
                .findFirst().orElseThrow(() -> new TeamNotFoundException(clubName,teamNumber));
        List<PlayerData> playersOfTeam = playerService.getAllPlayersOfTeam(teamData.id());
        EditTeamData editTeamData = new EditTeamData();
        editTeamData.setNumberOfBoards(8);
        editTeamData.setClubName(clubName);
        editTeamData.setTeamNumber(teamNumber);
        return editTeamData;
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
}
