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

    private final ClubService clubService;

    public TeamService(TeamRepository teamRepository,
                       DivisionService divisionService,
                       ClubService clubService) {
        this.teamRepository = teamRepository;
        this.divisionService = divisionService;
        this.clubService = clubService;
    }

    public Set<TeamData> getTeamsOfDivision(DivisionData divisionData) {
        return teamRepository.findByDivision_Id(divisionData.id())
                .stream()
                .map(this::toTeamData)
                .collect(Collectors.toSet());
    }

    public Integer getNumberOfTeamsOfDivision(DivisionData divisionData) {
        return teamRepository.findByDivision_Id(divisionData.id()).size();
    }

    public TeamData toTeamData(Team team) {
        return new TeamData(team.getId(),
                clubService.toClubData(team.getClub()),
                Optional.ofNullable(divisionService.toDivisionData(team.getDivision())),
                team.getNumber());
    }
}
