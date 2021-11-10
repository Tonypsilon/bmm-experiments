package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.berlinerschachverband.bmm.basedata.data.Division;
import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.DivisionRepository;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateDivisionData;
import de.berlinerschachverband.bmm.exceptions.DivisionAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.DivisionNotFoundException;
import de.berlinerschachverband.bmm.exceptions.NameBlankException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class DivisionService {

    private final DivisionRepository divisionRepository;

    private final SeasonService seasonService;

    public DivisionService(DivisionRepository divisionRepository, SeasonService seasonService) {
        this.divisionRepository = divisionRepository;
        this.seasonService = seasonService;
    }

    /**
     * Get all divisions names of a season, structured by level.
     * @param seasonName
     * @return
     */
    public SortedSetMultimap<Integer, String> getDivisionsOfSeasonByLevel(String seasonName) {
        SortedSetMultimap<Integer, String> divisionsByLevel = TreeMultimap.create();
        for(Division division : divisionRepository.findBySeason_Name(seasonName)) {
            divisionsByLevel.put(division.getLevel(), division.getName());
        }
        return divisionsByLevel;
    }

    /**
     * Get all divisions of a season
     * @param seasonData
     * @return
     */
    public Collection<DivisionData> getDivisionsOfSeason(SeasonData seasonData) {
        return divisionRepository.findBySeason_Id(seasonData.id())
                .stream()
                .map(this::toDivisionData)
                .collect(Collectors.toSet());
    }

    /**
     * Get a division by its name and its seasons name.
     * Similar to findDivisionByNameAndSeasonName from repository. However, this method should
     * only be called when the division really should exist.
     * @param divisionName
     * @param seasonName
     * @return
     */
    public Division getDivisionByNameAndSeasonName(String divisionName, String seasonName) {
        return divisionRepository.findByNameAndSeason_Name(divisionName,seasonName).orElseThrow(
                () -> new DivisionNotFoundException("season: " + seasonName + ", division: " + divisionName)
        );
    }

    /**
     * Create a division with a given name and a given level for a given season.
     * @param createDivisionData
     * @return
     */
    public DivisionData createDivision(CreateDivisionData createDivisionData) {
        if(createDivisionData.getName().isBlank()) {
            throw new NameBlankException();
        }
        if(divisionRepository.findByNameAndSeason_Name(
                createDivisionData.getName(),
                createDivisionData.getSeasonName())
                .isPresent()) {
            throw new DivisionAlreadyExistsException(
                    "season: " + createDivisionData.getSeasonName() +
                            ", division: " + createDivisionData.getName());
        }
        Division division = new Division();
        division.setName(createDivisionData.getName());
        division.setSeason(seasonService.getSeason(createDivisionData.getSeasonName()));
        division.setLevel(createDivisionData.getLevel());
        division.setNumberOfBoards(createDivisionData.getNumberOfBoards());
        divisionRepository.saveAndFlush(division);
        return toDivisionData(getDivisionByNameAndSeasonName(
                createDivisionData.getName(), createDivisionData.getSeasonName()));
    }

    public DivisionData toDivisionData(Division division) {
        return new DivisionData(division.getId(),
                division.getName(),
                division.getLevel(),
                division.getNumberOfBoards(),
                seasonService.toSeasonData(division.getSeason()));
    }
}
