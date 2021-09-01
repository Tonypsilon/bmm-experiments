package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final DivisionService divisionService;

    private final SeasonService seasonService;

    private final ClubService clubService;

    public TeamService(TeamRepository teamRepository,
                       DivisionService divisionService,
                       SeasonService seasonService,
                       ClubService clubService) {
        this.teamRepository = teamRepository;
        this.divisionService = divisionService;
        this.seasonService = seasonService;
        this.clubService = clubService;
    }

    public Set<TeamData> getTeamsOfDivision(DivisionData divisionData) {
        return teamRepository.findByDivision_Id(divisionData.id())
                .stream()
                .map(this::toTeamData)
                .collect(Collectors.toSet());
    }

    public TeamData toTeamData(Team team) {
        return new TeamData(team.getId(),
                seasonService.toSeasonData(team.getSeason()),
                clubService.toClubData(team.getClub()),
                team.getNumber(),
                Optional.ofNullable(team.getNextHigherTeam() == null ? null : toTeamData(team.getNextHigherTeam())),
                Optional.ofNullable(team.getNextLowerTeam() == null ? null : toTeamData(team.getNextLowerTeam())),
                divisionService.toDivisionData(team.getDivision()));
    }
}
