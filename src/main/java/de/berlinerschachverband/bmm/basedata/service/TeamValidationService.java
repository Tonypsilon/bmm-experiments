package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.Team;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.ValidatedTeamData;
import de.berlinerschachverband.bmm.resultdata.data.PlayerData;
import de.berlinerschachverband.bmm.resultdata.service.PlayerService;
import org.springframework.lang.NonNull;

import java.util.List;

public class TeamValidationService {

    private final TeamService teamService;
    private final PlayerService playerService;

    public TeamValidationService(TeamService teamService,
                                 PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    public List<ValidatedTeamData> getTeamsOfClubValidated(String clubName) {
        return teamService.getTeamsOfClub(clubName)
                .stream()
                .map(teamData -> new ValidatedTeamData(teamData, teamService.isLastTeam(teamData.id()), isValidTeam(teamData.id())))
                .toList();
    }

    @NonNull
    public Boolean isValidTeam(Long teamId) {
        Team team = teamService.getTeamById(teamId);
        Club club = team.getClub();
        List<TeamData> allTeamsOfClub = teamService.getTeamsOfClub(club.getName());
        List<Integer> playerNumbers = playerService.getAllPlayersOfTeam(teamId)
                .stream()
                .map(PlayerData::boardNumber)
                .sorted()
                .toList();
        if(Boolean.FALSE.equals(isPlayerNumberSequenceValid(playerNumbers))) {
            return false;
        }
        if(playerNumbers.size() > 32 ) {
            return false;
        }
        if(playerNumbers.size()>16 && Boolean.FALSE.equals(teamService.isLastTeam(teamId))) {
            return false;
        }

        return true;
    }

    @NonNull
    private Boolean isPlayerNumberSequenceValid(List<Integer> sortedPlayerNumbers) {
        for(int i=1; i<= sortedPlayerNumbers.size(); i++) {
            if(!sortedPlayerNumbers.get(i).equals(i)) {
                return false;
            }
        }
        return true;
    }
}
