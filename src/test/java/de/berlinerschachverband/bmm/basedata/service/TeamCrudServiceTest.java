package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.PlayerData;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.RemoveTeamsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TeamCrudServiceTest {

    private final TeamDataAccessService teamDataAccessService = mock(TeamDataAccessService.class);
    private final PlayerService playerService = mock(PlayerService.class);
    private TeamCrudService teamCrudService;

    private final ClubData club1 = new ClubData(1L, "club1", true, 1);
    private final TeamData teamData1 = new TeamData(1L, club1, Optional.empty(), 1, 8);
    private final TeamData teamData2 = new TeamData(2L, club1, Optional.empty(), 2, 8);
    private final PlayerData playerData1 = new PlayerData(1L,
            "Player One",
            "One",
            Optional.empty(),
            teamData2,
            12,
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            1,
            1);;
    private final PlayerData playerData2 = new PlayerData(1L,
            "Player Two",
            "Two",
            Optional.empty(),
            teamData2,
            17,
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            1,
            2);

    @BeforeEach
    private void setUp() {
        teamCrudService = new TeamCrudService(teamDataAccessService, playerService);
    }

    @Test
    void testCreateTeams() {
        when(teamDataAccessService.getTeamsOfClub("club1")).thenReturn(List.of(teamData1, teamData2));
        when(playerService.getAllPlayersOfTeam(2L)).thenReturn(List.of(playerData1, playerData2));
        CreateTeamsData createTeamsData = new CreateTeamsData();
        createTeamsData.setClubName("club1");
        createTeamsData.setNumberOfTeamsToCreate(2);

        teamCrudService.createTeams(createTeamsData);
        verify(teamDataAccessService, times(1)).createTeams(eq(createTeamsData));
        verify(playerService, never()).deletePlayer(eq(playerData1));
        verify(playerService, times(1)).deletePlayer(eq(playerData2));
    }

    @Test
    void testRemoveTeams() {
        when(teamDataAccessService.getTeamsOfClub("club1")).thenReturn(List.of(teamData1, teamData2));
        when(playerService.getAllPlayersOfTeam(2L)).thenReturn(List.of(playerData1, playerData2));
        RemoveTeamsData removeTeamsData = new RemoveTeamsData();
        removeTeamsData.setClubName("club1");
        removeTeamsData.setNumberOfTeamsToDelete(1);

        teamCrudService.removeTeams(removeTeamsData);
        verify(teamDataAccessService, times(1)).removeTeam(eq(teamData2));
        verify(teamDataAccessService, never()).removeTeam(eq(teamData1));
        verify(playerService, times(1)).deletePlayer(eq(playerData1));
        verify(playerService, times(1)).deletePlayer(eq(playerData2));
    }

}