package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.berlinerschachverband.bmm.basedata.data.*;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateDivisionData;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.exceptions.DivisionAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.DivisionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DivisionServiceTest {

    private final DivisionRepository divisionRepository = mock(DivisionRepository.class);
    private final SeasonService seasonService = mock(SeasonService.class);
    private DivisionService divisionService;
    private Division division1;
    private Division division2a;
    private Division division2b;
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
        division2a = new Division();
        division2a.setId(2L);
        division2a.setLevel(2);
        division2a.setSeason(season);
        division2a.setName("tivision2a");
        division2b = new Division();
        division2b.setId(3L);
        division2b.setLevel(2);
        division2b.setSeason(season);
        division2b.setName("division2b");
    }

    @Test
    void testGetDivisionsOfSeasonByLevel() {
        when(divisionRepository.findBySeason_Name(season.getName())).thenReturn(
                List.of(division1, division2a, division2b)
        );
        SortedSetMultimap<Integer, String> expected = TreeMultimap.create();
        expected.put(division2b.getLevel(), division2b.getName());
        expected.put(division1.getLevel(), division1.getName());
        expected.put(division2a.getLevel(), division2a.getName());
        assertEquals(expected, divisionService.getDivisionsOfSeasonByLevel(season.getName()));
    }

    @Test
    void testGetDivisionsOfSeason() {
        when(divisionRepository.findBySeason_Id(1L)).thenReturn(
                List.of(division1, division2a)
        );
        when(seasonService.toSeasonData(season)).thenReturn(new SeasonData(1L, "season"));
        assertEquals(Set.of(
                new DivisionData(1L, "division1", 1, new SeasonData(1L, "season")),
                new DivisionData(2L, "tivision2a", 2, new SeasonData(1L, "season"))
        ),
                divisionService.getDivisionsOfSeason(new SeasonData(1L, "season")));
    }

    @Test
    void testGetDivisionByNameAndSeasonName() {
        when(divisionRepository.findByNameAndSeason_Name("division1", "season1"))
                .thenReturn(Optional.of(division1));
        when(divisionRepository.findByNameAndSeason_Name("division2", "season2"))
                .thenReturn(Optional.empty());

        assertEquals(divisionService.getDivisionByNameAndSeasonName("division1", "season1"),
                division1);
        BmmException exception = assertThrows(DivisionNotFoundException.class, () -> divisionService.getDivisionByNameAndSeasonName("division2", "season2"));
        assertEquals(exception.getMessage(), "season: season2, division: division2");
    }

    @Test
    void testCreateDivision() {
        CreateDivisionData createDivisionData1 = new CreateDivisionData();
        createDivisionData1.setName("division2b");
        createDivisionData1.setSeasonName("season");
        createDivisionData1.setLevel(5);

        CreateDivisionData createDivisionData2 = new CreateDivisionData();
        createDivisionData2.setName("tivision2a");
        createDivisionData2.setSeasonName("season");
        createDivisionData2.setLevel(2);

        when(divisionRepository.findByNameAndSeason_Name("division2b", "season"))
                .thenReturn(Optional.of(division2b));
        when(divisionRepository.findByNameAndSeason_Name("tivision2a", "season"))
                .thenReturn(Optional.empty(), Optional.of(division2a));
        when(seasonService.getSeason("season")).thenReturn(season);
        when(seasonService.toSeasonData(season)).thenReturn(new SeasonData(season.getId(), season.getName()));

        BmmException exception = assertThrows(DivisionAlreadyExistsException.class,
                () -> divisionService.createDivision(createDivisionData1));
        assertEquals("season: season, division: division2b", exception.getMessage());

        assertEquals(new DivisionData(division2a.getId(),
                        division2a.getName(),
                        division2a.getLevel(),
                        new SeasonData(division2a.getSeason().getId(), division2a.getSeason().getName())),
                divisionService.createDivision(createDivisionData2));
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