package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.berlinerschachverband.bmm.basedata.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DivisionServiceTest {

    private final DivisionRepository divisionRepository = mock(DivisionRepository.class);
    private final SeasonService seasonService = mock(SeasonService.class);
    private DivisionService divisionService;
    private Division division1;
    private Season season;

    @BeforeEach
    private void setUp() {
        divisionService = new DivisionService(divisionRepository, seasonService);
        season = new Season();
        season.setId(1L);
        season.setName("season");
        division1 = new Division();
        division1.setId(1L);
        division1.setLevel(1);
        division1.setSeason(season);
        division1.setName("division1");
    }

    @Test
    void testGetDivisionsOfSeasonByLevel() {
        Division division2a = new Division();
        division2a.setId(2L);
        division2a.setLevel(2);
        division2a.setSeason(season);
        division2a.setName("tivision2a");
        Division division2b = new Division();
        division2b.setId(3L);
        division2b.setLevel(2);
        division2b.setSeason(season);
        division2b.setName("division2b");
        when(divisionRepository.findBySeason_Name(season.getName())).thenReturn(
                List.of(division1,division2a,division2b)
        );
        SortedSetMultimap<Integer, String> expected = TreeMultimap.create();
        expected.put(division2b.getLevel(), division2b.getName());
        expected.put(division1.getLevel(), division1.getName());
        expected.put(division2a.getLevel(), division2a.getName());
        assertEquals(expected, divisionService.getDivisionsOfSeasonByLevel(season.getName()));
    }

    @Test
    void testToDivisionData() {
        when(seasonService.toSeasonData(season)).thenReturn(new SeasonData(season.getId(), season.getName()));
        DivisionData actual = divisionService.toDivisionData(division1);
        assertEquals(actual.id(), division1.getId());
        assertEquals(actual.name(), division1.getName());
        assertEquals(actual.season(), new SeasonData(season.getId(), season.getName()));
    }

}