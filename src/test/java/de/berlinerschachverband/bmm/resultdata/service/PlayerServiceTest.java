package de.berlinerschachverband.bmm.resultdata.service;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.Team;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.service.TeamService;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.resultdata.data.AvailablePlayer;
import de.berlinerschachverband.bmm.resultdata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.resultdata.data.PlayerRepository;
import de.berlinerschachverband.bmm.resultdata.data.thymeleaf.PlayerAssignmentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerServiceTest {

    private final TeamService teamService = mock(TeamService.class);
    private final AvailablePlayerService availablePlayerService = mock(AvailablePlayerService.class);
    private final PlayerRepository playerRepository = mock(PlayerRepository.class);

    private PlayerService playerService;

    private ClubData club;
    AvailablePlayerData availablePlayerData;

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
    }

    @Test
    void testAssignPlayerToTeamLess1() {
        BmmException exception = assertThrows(BmmException.class,
                () -> {
                    playerService.assignPlayerToTeam(
                            new PlayerAssignmentData(1,1,0),
                            new TeamData(1L, club, Optional.empty(), 1));
                });
        assertEquals("board number < 1",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamGreater32() {
        BmmException exception = assertThrows(BmmException.class,
                () -> {
            playerService.assignPlayerToTeam(
                    new PlayerAssignmentData(1,1,33),
                    new TeamData(1L, club, Optional.empty(), 1));
                });
        assertEquals("board number > 32",
                exception.getMessage());
    }

    @Test
    void testAssignPlayerToTeamGreater16NotLastTeam() {
        when(availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1))
                .thenReturn(availablePlayerData);
        Team team = new Team();
        team.setId(1L);
        when(teamService.getTeamById(1L)).thenReturn(team);
        BmmException exception = assertThrows(BmmException.class,
                () -> {
            playerService.assignPlayerToTeam(
                    new PlayerAssignmentData(1, 1,17),
                    new TeamData(1L, club, Optional.empty(), 1)
            );
                });
        assertEquals("Only last team of club can have more than 16 members.",
                exception.getMessage());
    }

}