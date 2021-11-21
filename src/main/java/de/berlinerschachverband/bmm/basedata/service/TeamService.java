package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.RemoveTeamsData;
import de.berlinerschachverband.bmm.exceptions.TeamAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.TeamNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
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

    /**
     * Gets all teams of a particular division.
     *
     * @param divisionData
     * @return
     */
    public Set<TeamData> getTeamsOfDivision(DivisionData divisionData) {
        return teamRepository.findByDivision_Id(divisionData.id())
                .stream()
                .map(this::toTeamData)
                .collect(Collectors.toSet());
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> {
                   throw new TeamNotFoundException(id);
                });
    }

    /**
     * Gets all available teams (not having a division and hence a season yet)
     * given the club name - ordered by team number.
     *
     * @param clubName
     * @return
     */
    public List<TeamData> getTeamsOfClub(String clubName) {
        return teamRepository.findByClub_NameAndDivisionIsNull(clubName).stream()
                .map(this::toTeamData)
                .sorted(Comparator.comparing(TeamData::number))
                .toList();
    }

    /**
     * Get the number of teams of a particular division.
     *
     * @param divisionData
     * @return
     */
    public Integer getNumberOfTeamsOfDivision(DivisionData divisionData) {
        return teamRepository.findByDivision_Id(divisionData.id()).size();
    }

    /**
     * Given the name of a club and a number, creates a team for that club with the
     * given number, accordingly. Throws a TeamAlreadyExistsException if the club
     * already has a team of the given number.
     *
     * @param createTeamData
     * @return
     */
    private TeamData createTeam(CreateTeamData createTeamData) {
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

    /**
     * Given the name of a club and a number, add this many teams for the club.
     *
     * @param createTeamsData
     */
    public void createTeams(CreateTeamsData createTeamsData) {
        int currentNumberOfTeams = teamRepository.findByClub_NameAndDivisionIsNull(createTeamsData.getClubName()).size();
        for (int teamNumber = 1; teamNumber <= createTeamsData.getNumberOfTeamsToCreate(); teamNumber++) {
            Team team = new Team();
            team.setClub(clubService.getClub(createTeamsData.getClubName()));
            team.setNumber(teamNumber + currentNumberOfTeams);
            teamRepository.saveAndFlush(team);
        }
    }

    public void removeTeams(RemoveTeamsData removeTeamsData) {
        int currentNumberOfTeams = teamRepository.findByClub_NameAndDivisionIsNull(removeTeamsData.getClubName()).size();
        for(int teamNumber = currentNumberOfTeams;
            teamNumber > currentNumberOfTeams - removeTeamsData.getNumberOfTeamsToDelete();
            teamNumber--) {
            teamRepository.findByClub_NameAndNumberAndDivisionIsNull(
                    removeTeamsData.getClubName(),
                    teamNumber
            ).ifPresent(
                    team -> {
                        teamRepository.delete(team);
                        teamRepository.flush();
                    });
        }
    }

    @NonNull
    public Boolean isLastTeam(Long teamId) {
        Team team = getTeamById(teamId);
        Club club = team.getClub();
        Collection<TeamData> allTeamsOfClub = getTeamsOfClub(club.getName());
        Integer highestTeamNumber = allTeamsOfClub
                .stream()
                .map(TeamData::number)
                .reduce(Integer::max)
                .orElseThrow(); // should never happen as the originally provided team must always be part of the stream.
        return highestTeamNumber.equals(team.getNumber());
    }

    /**
     * Convert a Team to TeamData.
     *
     * @param team
     * @return
     */
    public TeamData toTeamData(Team team) {
        return new TeamData(team.getId(),
                clubService.toClubData(team.getClub()),
                team.getDivision().isPresent() ? Optional.of(divisionService.toDivisionData(team.getDivision().get())) : Optional.empty(),
                team.getNumber());
    }
}
