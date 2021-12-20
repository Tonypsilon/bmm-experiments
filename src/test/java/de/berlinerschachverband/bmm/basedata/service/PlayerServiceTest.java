package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.basedata.data.Player;
import de.berlinerschachverband.bmm.basedata.data.PlayerRepository;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PlayerAssignmentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    private final TeamService teamService = mock(TeamService.class);
    private final AvailablePlayerService availablePlayerService = mock(AvailablePlayerService.class);
    private final PlayerRepository playerRepository = mock(PlayerRepository.class);

    private PlayerService playerService;

    private ClubData club;
    private AvailablePlayerData availablePlayerData;
    private Team team;

    @BeforeEach
    private void setUp() {
        playerService = new PlayerService(teamService, availablePlayerService, playerRepository);
        club = new ClubData(1L, "club", true, 1);
        availablePlayerData = new AvailablePlayerData(
          1L,
          1,
          1,
          'A',
          "full name",
          "name",
          1901,
          Optional.of(1234),
          Optional.empty(),
          Optional.empty()
        );
        team = new Team();
        team.setId(1L);
    }

    @Test
    void testAssignPlayerToTeamLess1() {
        BmmException exception = assertThrows(BmmException.class,
                () -> {
                    playerService.assignPlayerToTeam(
                            new PlayerAssignmentData(1,1,0),
                            new TeamData(1L, club, Optional.empty(), 1, 8));
                });
        assertEquals("board boardNumber < 1",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamGreater32() {
        BmmException exception = assertThrows(BmmException.class,
                () -> {
            playerService.assignPlayerToTeam(
                    new PlayerAssignmentData(1,1,33),
                    new TeamData(1L, club, Optional.empty(), 1, 8));
                });
        assertEquals("board boardNumber > 32",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamAlreadyDivision() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        team.setDivision(new Division());
        when(teamService.getTeamById(1L)).thenReturn(team);

        BmmException exception = assertThrows(BmmException.class,
                () -> {
            playerService.assignPlayerToTeam(
                    new PlayerAssignmentData(1, 1, 1),
                    new TeamData(1L,
                            club,
                            Optional.of(new DivisionData(
                                    1L,
                                    "division",
                                    1,
                                    8,
                                    new SeasonData(1L, "season", false))),
                            1,
                            8)
            );
                });
        assertEquals("Player can not be assigned to team that already is in a division.",exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamNUmberAlreadyOnTeam() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        when(teamService.getTeamById(1L)).thenReturn(team);
        when(playerRepository.findByTeam_IdAndBoardNumber(1L,2)).thenReturn(Optional.of(new Player()));

        BmmException exception = assertThrows(BmmException.class,
                () -> {
            playerService.assignPlayerToTeam(
                    new PlayerAssignmentData(1, 1, 2),
                    new TeamData(1L, club, Optional.empty(), 1, 8)
            );
                });
        assertEquals("Player with that boardNumber is already on the team.",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamGreater16NotLastTeam() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        when(playerRepository.findByZpsAndAndMemberNumber(1,1)).thenReturn(Optional.empty());
        Team team = new Team();
        team.setId(1L);
        when(teamService.getTeamById(1L)).thenReturn(team);
        BmmException exception = assertThrows(BmmException.class,
                () -> {
            playerService.assignPlayerToTeam(
                    new PlayerAssignmentData(1, 1,17),
                    new TeamData(1L, club, Optional.empty(), 1, 8)
            );
                });
        assertEquals("Only last team of club can have more than 16 members.",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamNotInClub() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        when(teamService.getTeamById(1L)).thenReturn(team);
        when(playerRepository.findByTeam_IdAndBoardNumber(1L,2)).thenReturn(Optional.empty());
        when(playerRepository.findByZpsAndAndMemberNumber(1,1)).thenReturn(Optional.empty());

        BmmException exception = assertThrows(BmmException.class,
                () -> {
                    playerService.assignPlayerToTeam(
                            new PlayerAssignmentData(2, 1, 2),
                            new TeamData(1L, club, Optional.empty(), 1, 8)
                    );
                });
        assertEquals("The player is not a member of the club.",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamPlayerAlreadyAssigned() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        when(teamService.getTeamById(1L)).thenReturn(team);
        when(playerRepository.findByTeam_IdAndBoardNumber(1L,2)).thenReturn(Optional.empty());
        when(playerRepository.findByZpsAndAndMemberNumber(1,1)).thenReturn(Optional.of(new Player()));

        BmmException exception = assertThrows(BmmException.class,
                () -> {
                    playerService.assignPlayerToTeam(
                            new PlayerAssignmentData(1, 1, 2),
                            new TeamData(1L, club, Optional.empty(), 1, 8)
                    );
                });
        assertEquals("This player is already on a team.",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamSuccess() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        when(teamService.getTeamById(1L)).thenReturn(team);
        when(playerRepository.findByTeam_IdAndBoardNumber(1L,2)).thenReturn(Optional.empty());

        playerService.assignPlayerToTeam(
                new PlayerAssignmentData(1,1,2),
                new TeamData(1L, club, Optional.empty(), 1, 8)
        );
        verify(playerRepository, times(1)).saveAndFlush(argThat(
                player -> player.getZps().equals(1)
        && player.getMemberNumber().equals(1)
        && player.getFullName().equals("full name")
        && player.getSurname().equals("name")
        && player.getDwz().isPresent() && player.getDwz().get().equals(1234)
        && player.getTeam().equals(team)
        && player.getTitle().isEmpty()
        && player.getElo().isEmpty()));

    }

}