package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.AvailablePlayer;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerRepository;
import de.berlinerschachverband.bmm.exceptions.AvailablePlayerNotFoundException;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailablePlayerServiceTest {

    private AvailablePlayerService availablePlayerService;
    private final AvailablePlayerRepository availablePlayerRepository = mock(AvailablePlayerRepository.class);

    @BeforeEach
    private void setUp() {
        availablePlayerService = new AvailablePlayerService(availablePlayerRepository);

    }

    @Test
    void testGetAvailablePlayerByZpsAndMemberNumber() {
        AvailablePlayer availablePlayer = new AvailablePlayer();
        availablePlayer.setId(1L);
        availablePlayer.setZps(1);
        availablePlayer.setFullName("test");
        when(availablePlayerRepository.findByZpsAndMemberNumber(1,1)).thenReturn(Optional.of(availablePlayer));
        when(availablePlayerRepository.findByZpsAndMemberNumber(1,2)).thenReturn(Optional.empty());

        AvailablePlayerData actual = availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,1);
        assertEquals(1L, actual.id());
        assertEquals(1, actual.zps());
        assertEquals("test", actual.fullName());

        BmmException exception = assertThrows(AvailablePlayerNotFoundException.class,
                () -> availablePlayerService.getAvailablePlayerByZpsAndMemberNumber(1,2));
        assertEquals("Available player does not exist, zps: 1, memberNumber: 2.", exception.getMessage());

        verify(availablePlayerRepository).findByZpsAndMemberNumber(1, 1);
        verify(availablePlayerRepository).findByZpsAndMemberNumber(1, 2);
        verifyNoMoreInteractions(availablePlayerRepository);
    }

    @Test
    void testGetAvailablePlayerByZps() {
        AvailablePlayer availablePlayer1 = new AvailablePlayer();
        availablePlayer1.setId(1L);
        availablePlayer1.setZps(1);
        availablePlayer1.setSurname("test1");
        AvailablePlayer availablePlayer2 = new AvailablePlayer();
        availablePlayer2.setId(2L);
        availablePlayer2.setZps(1);
        availablePlayer2.setSurname("test2");
        Collection<AvailablePlayer> availablePlayers = List.of(availablePlayer2, availablePlayer1);
        when(availablePlayerRepository.findByZps(1)).thenReturn(availablePlayers);
        when(availablePlayerRepository.findByZps(2)).thenReturn(Collections.emptyList());

        List<AvailablePlayerData> actual = availablePlayerService.getAvailablePlayersByZps(1);
        assertEquals(2, actual.size());
        assertEquals(1L, actual.get(0).id());
        assertEquals(2L, actual.get(1).id());

        actual = availablePlayerService.getAvailablePlayersByZps(2);
        assertEquals(0, actual.size());

        verify(availablePlayerRepository).findByZps(1);
        verify(availablePlayerRepository).findByZps(2);
        verifyNoMoreInteractions(availablePlayerRepository);
    }

    @Test
    void testDeleteAvailablePlayer() {
        AvailablePlayer availablePlayer = new AvailablePlayer();
        availablePlayer.setId(1L);
        availablePlayer.setZps(1);
        availablePlayer.setFullName("test");
        when(availablePlayerRepository.findByZpsAndMemberNumber(1,1)).thenReturn(Optional.of(availablePlayer));
        when(availablePlayerRepository.findByZpsAndMemberNumber(1,2)).thenReturn(Optional.empty());
        doNothing().when(availablePlayerRepository).delete(any());

        availablePlayerService.deleteAvailablePlayer(1, 1);
        verify(availablePlayerRepository).delete(availablePlayer);
        AvailablePlayerNotFoundException exception = assertThrows(AvailablePlayerNotFoundException.class,
                () -> availablePlayerService.deleteAvailablePlayer(1,2));
        assertEquals("Available player does not exist, zps: 1, memberNumber: 2.", exception.getMessage());
        verify(availablePlayerRepository).findByZpsAndMemberNumber(1, 1);
        verify(availablePlayerRepository).findByZpsAndMemberNumber(1, 2);
        verifyNoMoreInteractions(availablePlayerRepository);
    }

}