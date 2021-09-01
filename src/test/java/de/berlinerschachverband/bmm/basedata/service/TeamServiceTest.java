package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    private final TeamRepository teamRepository = mock(TeamRepository.class);
    private final DivisionService divisionService = mock(DivisionService.class);
    private final SeasonService seasonService = mock(SeasonService.class);
    private final ClubService clubService = mock(ClubService.class);
    private TeamService teamService;

    @BeforeEach
    private void setUp() {
        teamService = new TeamService(teamRepository, divisionService, seasonService, clubService);
    }

    @Test
    void testGetTeamsOfDivision() {
        SeasonData season1 = new SeasonData(1L, "season1");
        Set<TeamData> expected = Set.of(
                new TeamData(1L,
                        season1,
                        new ClubData(1L, "club1", season1),
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        new DivisionData(1L, "division1", 1, season1)),
                new TeamData(2L,
                        season1,
                        new ClubData(2L, "club2", season1),
                        1,
                        Optional.empty(),
                        Optional.empty(),
                        new DivisionData(1L, "division1", 1, season1))
        );
        Season season = new Season();
        season.setName("season1");
        Division division = new Division();
        division.setId(1L);
        division.setName("division1");
        division.setLevel(1);
        division.setSeason(season);
        Club club1 = new Club();
        club1.setId(1L);
        club1.setName("club1");
        club1.setSeason(season);
        Club club2 = new Club();
        club2.setName("club2");
        club2.setSeason(season);

        Team team1 = new Team();
        team1.setId(1L);
        team1.setSeason(season);
        team1.setClub(club1);
        team1.setDivision(division);
        team1.setNumber(1);
        Team team2 = new Team();
        team2.setId(2L);
        team2.setSeason(season);
        team2.setClub(club2);
        team2.setDivision(division);
        team2.setNumber(1);
        when(teamRepository.findByDivision_Id(1L)).thenReturn(Set.of(team1, team2));
        when(seasonService.toSeasonData(season)).thenReturn(season1);
        when(clubService.toClubData(club1)).thenReturn(new ClubData(1L, "club1", season1));
        when(clubService.toClubData(club2)).thenReturn(new ClubData(2L, "club2", season1));
        when(divisionService.toDivisionData(division)).thenReturn(new DivisionData(1L, "division1", 1, season1));

        assertEquals(expected, teamService.getTeamsOfDivision(new DivisionData(1L, "division1", 1, season1)));
    }

}