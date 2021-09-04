package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Season;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.SeasonRepository;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.exceptions.SeasonAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.SeasonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SeasonServiceTest {

    private final SeasonRepository seasonRepository = mock(SeasonRepository.class);
    private SeasonService seasonService;
    private Season season1;
    private Season season2;

    @BeforeEach
    private void setUp() {
        seasonService = new SeasonService(seasonRepository);
        season1 = new Season();
        season1.setId(1L);
        season1.setName("zeason1"); // test alphabetical sorting as well
        season2 = new Season();
        season2.setId(2L);
        season2.setName("season2");
    }

    @Test
    void testGetAllSeasonsAndAllSeasonNames() {
        when(seasonRepository.findAll()).thenReturn(List.of(season1, season2));
        List<SeasonData> actualGetAllSeasons = seasonService.getAllSeasons();
        assertEquals(actualGetAllSeasons,
                List.of(new SeasonData(season2.getId(), season2.getName()),
                        new SeasonData(season1.getId(), season1.getName())));
        List<String> actualGetSeasonNames = seasonService.getSeasonNames();
        assertEquals(actualGetSeasonNames, List.of("season2", "zeason1"));
    }

    @Test
    void testGetSeason() {
        when(seasonRepository.findByName("zeason1")).thenReturn(Optional.of(season1));
        when(seasonRepository.findByName("blabla")).thenReturn(Optional.empty());

        assertEquals(seasonService.getSeason("zeason1"), season1);
        BmmException exception = assertThrows(SeasonNotFoundException.class, () -> seasonService.getSeason("blabla"));
        assertEquals(exception.getMessage(), "blabla");
    }

    @Test
    void testCreateSeason() {
        when(seasonRepository.findByName("zeason1")).thenReturn(Optional.of(season1));
        when(seasonRepository.findByName("season2")).thenReturn(Optional.empty(), Optional.of(season2));

        BmmException exception = assertThrows(SeasonAlreadyExistsException.class, () -> seasonService.createSeason("zeason1"));
        assertEquals(exception.getMessage(), "zeason1");

        assertEquals(seasonService.createSeason("season2"), new SeasonData(season2.getId(), season2.getName()));
    }


}