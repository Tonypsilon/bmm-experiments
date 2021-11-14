package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.ClubRepository;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.exceptions.ClubAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.ClubNotFoundException;
import de.berlinerschachverband.bmm.exceptions.NameBlankException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClubServiceTest {

    private final ClubRepository clubRepository = mock(ClubRepository.class);
    private ClubService clubService;
    private Club club1, club2;
    private final Club club3 = mock(Club.class);

    @BeforeEach
    private void setUp() {
        clubService = new ClubService(clubRepository);
        club1 = new Club();
        club1.setId(1L);
        club1.setName("club1");
        club1.setActive(true);
        club1.setZps(1);
        club2 = new Club();
        club2.setId(2L);
        club2.setName("club2");
        club2.setActive(false);
        club2.setZps(2);
    }

    @Test
    void testGetAllClubs() {
        when(clubRepository.findAll()).thenReturn(List.of(club2, club1));
        assertEquals(List.of(
                new ClubData(1L, "club1", true, 1),
                new ClubData(2L, "club2", false, 2)
                ),
                clubService.getAllClubs());
    }

    @Test
    void testGetAllActiveClubs() {
        when(clubRepository.findByActiveTrue()).thenReturn(List.of(club1));
        assertEquals(List.of(new ClubData(1L, "club1", true, 1)),
                clubService.getAllActiveClubs());
    }

    @Test
    void testGetAllActiveClubsNames() {
        when(clubRepository.findByActiveTrue()).thenReturn(List.of(club1));
        assertEquals(List.of("club1"),
                clubService.getAllActiveClubsNames());
    }

    @Test
    void testGetClub() {
        when(clubRepository.findByName("club1")).thenReturn(Optional.of(club1));
        when(clubRepository.findByName("club3")).thenReturn(Optional.empty());

        assertEquals(club1, clubService.getClub("club1"));
        BmmException exception = assertThrows(ClubNotFoundException.class, () -> clubService.getClub("club3"));
        assertEquals("club3", exception.getMessage());
    }

    @Test
    void testCreateClub() {
        assertThrows(NameBlankException.class, () -> clubService.createClub("",3));

        when(clubRepository.findByName("club1")).thenReturn(Optional.of(club1));
        when(clubRepository.findByName("club2"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(club2));

        BmmException exception = assertThrows(ClubAlreadyExistsException.class, () -> clubService.createClub("club1", 1));
        assertEquals("club1", exception.getMessage());
        assertEquals(new ClubData(2L, "club2", false, 2), clubService.createClub("club2", 2));
    }

    @Test
    void testActivateClub() {
        when(clubRepository.findByName("club3")).thenReturn(Optional.of(club3));
        clubService.activateClub("club3");
        verify(club3, times(1)).setActive(true);
        verify(clubRepository, times(1)).saveAndFlush(club3);
    }

    @Test
    void testDeactivateClub() {
        when(clubRepository.findByName("club3")).thenReturn(Optional.of(club3));
        clubService.deactivateClub("club3");
        verify(club3, times(1)).setActive(false);
        verify(clubRepository, times(1)).saveAndFlush(club3);
    }
}
