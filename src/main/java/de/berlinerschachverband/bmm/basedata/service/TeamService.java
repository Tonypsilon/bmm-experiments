package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.exceptions.TeamAlreadyExistsException;
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

    public TeamData createTeam(CreateTeamData createTeamData) {
        if(teamRepository.findByClub_NameAndNumberAndDivisionIsNull(
                createTeamData.getClubName(), createTeamData.getNumber()).isPresent()) {
            throw new TeamAlreadyExistsException("club: " + createTeamData.getClubName() + ", number: "
            + createTeamData.getNumber());
        }
        Team team = new Team();
        team.setClub(clubService.getClub(createTeamData.getClubName()));
        team.setNumber(createTeamData.getNumber());
        teamRepository.saveAndFlush(team);
        return toTeamData(teamRepository.findByClub_NameAndNumberAndDivisionIsNull(
                createTeamData.getClubName(), createTeamData.getNumber()
        ).get());
    }

    public void createTeams(CreateTeamsData createTeamsData) {
        for (int teamNumber = 1; teamNumber <= createTeamsData.getNumberOfTeams(); teamNumber++) {
            try {
                CreateTeamData createTeamData = new CreateTeamData();
                createTeamData.setClubName(createTeamsData.getClubName());
                createTeamData.setNumber(teamNumber);
                createTeam(createTeamData);
            } catch(TeamAlreadyExistsException ex) {
                continue;
            }
        }
    }

    public TeamData toTeamData(Team team) {
        return new TeamData(team.getId(),
                clubService.toClubData(team.getClub()),
                team.getDivision().isPresent() ?  Optional.of(divisionService.toDivisionData(team.getDivision().get())) : Optional.empty(),
                team.getNumber());
    }
}
