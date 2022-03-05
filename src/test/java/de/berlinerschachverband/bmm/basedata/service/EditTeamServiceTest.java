package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PlayerAssignmentData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PrepareEditTeamData;
import de.berlinerschachverband.bmm.exceptions.TeamNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTeamServiceTest {

    private EditTeamService editTeamService;
    private final TeamDataAccessService teamDataAccessService = mock(TeamDataAccessService.class);
    private final PlayerService playerService = mock(PlayerService.class);
    private final AvailablePlayerService availablePlayerService = mock(AvailablePlayerService.class);
    private final ClubService clubService = mock(ClubService.class);

    private Club club;
    private ClubData clubData;
    private TeamData teamData1, teamData2;
    private PlayerData playerData1, playerData2;
    private AvailablePlayerData availablePlayerData1, availablePlayerData2;

    @BeforeEach
    private void setUp()  {
        editTeamService = new EditTeamService(teamDataAccessService,
                playerService,
                availablePlayerService,
                clubService);
        club = new Club();
        club.setId(1L);
        club.setName("club1");
        club.setActive(true);
        club.setZps(1);
        clubData = new ClubData(1L, "club1", true, 1);
        teamData1 = new TeamData(1L, clubData, Optional.empty(), 1, 8);
        teamData2 = new TeamData(2L, clubData, Optional.empty(), 2, 8);
        playerData1 = new PlayerData(1L,
                "Spieler Eins",
                "Eins",
                Optional.empty(),
                teamData1,
                1,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                1,
                1);
        playerData2 = new PlayerData(2L,
                "Spieler Zwei",
                "Zwei",
                Optional.empty(),
                teamData2,
                1,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                1,
                2);
        availablePlayerData1 = new AvailablePlayerData(1L,
                1,
                1,
                'A',
                "Spieler Eins",
                "Eins",
                1900,
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
        availablePlayerData2 = new AvailablePlayerData(2L,
                1,
                2,
                'A',
                "Spieler Zwei",
                "Zwei",
                1901,
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    @Test
    void testGetTeamForEditingSuccess() {
        when(teamDataAccessService.getTeamsOfClub("club1")).thenReturn(List.of(teamData1, teamData2));
        when(playerService.getAllPlayersOfTeam(2L)).thenReturn(List.of(playerData2));
        when(teamDataAccessService.isLastTeam(2L)).thenReturn(true);
        when(clubService.getClub("club1")).thenReturn(club);
        when(availablePlayerService.getAvailablePlayersByZps(1)).thenReturn(List.of(availablePlayerData1,availablePlayerData2));

        PrepareEditTeamData actual = editTeamService.getTeamForEditing("club1", 2);
        verify(teamDataAccessService).getTeamsOfClub("club1");
        verify(teamDataAccessService).isLastTeam(2L);
        verifyNoMoreInteractions(teamDataAccessService);
        verify(playerService).getAllPlayersOfTeam(2L);
        verifyNoMoreInteractions(playerService);
        verify(clubService).getClub("club1");
        verifyNoMoreInteractions(clubService);
        verify(availablePlayerService).getAvailablePlayersByZps(1);
        verifyNoMoreInteractions(availablePlayerService);

        assertEquals(3, actual.getAvailablePlayers().size());
        assertEquals(-1, actual.getAvailablePlayers().get(0).getMemberNumber());
        assertEquals(1, actual.getAvailablePlayers().get(1).getMemberNumber());
        assertEquals(2, actual.getAvailablePlayers().get(2).getMemberNumber());

        assertEquals(32, actual.getMaxNumberOfPlayers());
        assertEquals("club1", actual.getClubName());
        assertEquals(2, actual.getTeamNumber());
        assertEquals(1, actual.getCurrentTeamPlayers().size());
        assertEquals(2, actual.getCurrentTeamPlayers().get(0).getMemberNumber());

        assertEquals(32, actual.getFutureTeamPlayersMemberNumbers().size());
        for(int i=0; i<32; i++) {
            assertEquals(-1, actual.getFutureTeamPlayersMemberNumbers().get(i));
        }
    }

    @Test
    void testGetTeamForEditingFailure() {
        when(teamDataAccessService.getTeamsOfClub("club1")).thenReturn(List.of(teamData1, teamData2));
        TeamNotFoundException exception = assertThrows(TeamNotFoundException.class, () -> editTeamService.getTeamForEditing("club1", 3));
        assertEquals("Team number 3 not found for club club1.", exception.getMessage());
        verify(teamDataAccessService).getTeamsOfClub("club1");
        verifyNoMoreInteractions(teamDataAccessService);
        verifyNoInteractions(playerService, availablePlayerService, clubService);
    }

    @Test
    void testEditTeamSuccess() {
        when(teamDataAccessService.getTeamsOfClub("club1")).thenReturn(List.of(teamData1, teamData2));
        when(playerService.getAllPlayersOfTeam(1L)).thenReturn(List.of(playerData1));
        when(clubService.getClub("club1")).thenReturn(club);
        PrepareEditTeamData prepareEditTeamData = new PrepareEditTeamData();
        prepareEditTeamData.setTeamNumber(1);
        prepareEditTeamData.setClubName("club1");
        prepareEditTeamData.setFutureTeamPlayersMemberNumber1(3);
        editTeamService.editTeam(prepareEditTeamData);
        verify(teamDataAccessService).getTeamsOfClub("club1");
        verify(playerService).getAllPlayersOfTeam(1L);
        verify(clubService).getClub("club1");
        verify(playerService).deletePlayer(playerData1);
        verify(playerService).assignPlayerToTeam(argThat(playerAssignmentData -> {
            return playerAssignmentData.zps().equals(1)
                    && playerAssignmentData.memberNumber().equals(3)
                    && playerAssignmentData.boardNumber().equals(1);
        }),eq(teamData1));
        verifyNoMoreInteractions(teamDataAccessService,playerService,clubService);
    }

    @Test
    void testEditTeamFailure() {
        PrepareEditTeamData prepareEditTeamData = new PrepareEditTeamData();
        prepareEditTeamData.setClubName("club1");
        prepareEditTeamData.setTeamNumber(3);
        when(teamDataAccessService.getTeamsOfClub("club1")).thenReturn(List.of(teamData1, teamData2));
        TeamNotFoundException exception = assertThrows(TeamNotFoundException.class, () -> editTeamService.editTeam(prepareEditTeamData));
        assertEquals("Team number 3 not found for club club1.", exception.getMessage());
        verify(teamDataAccessService).getTeamsOfClub("club1");
        verifyNoMoreInteractions(teamDataAccessService);
        verifyNoInteractions(playerService, availablePlayerService, clubService);
    }

}