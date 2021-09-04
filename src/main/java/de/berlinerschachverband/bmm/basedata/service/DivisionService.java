package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.berlinerschachverband.bmm.basedata.data.Division;
import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.DivisionRepository;
import de.berlinerschachverband.bmm.exceptions.DivisionAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.DivisionNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DivisionService {

    private final DivisionRepository divisionRepository;

    private final SeasonService seasonService;

    public DivisionService(DivisionRepository divisionRepository, SeasonService seasonService) {
        this.divisionRepository = divisionRepository;
        this.seasonService = seasonService;
    }

    /**
     * Get all divisions of a season, structured by level.
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
     * @param divisionName
     * @param level
     * @param seasonName
     * @return
     */
    public DivisionData createDivision(String divisionName, Integer level, String seasonName) {
        if(divisionRepository.findByNameAndSeason_Name(divisionName, seasonName).isPresent()) {
            throw new DivisionAlreadyExistsException(
                    "season: " + seasonName + ", division: " + divisionName);
        }
        Division division = new Division();
        division.setName(divisionName);
        division.setSeason(seasonService.getSeason(seasonName));
        division.setLevel(level);
        divisionRepository.saveAndFlush(division);
        return toDivisionData(getDivisionByNameAndSeasonName(divisionName, seasonName));
    }

    public DivisionData toDivisionData(Division division) {
        return new DivisionData(division.getId(),
                division.getName(),
                division.getLevel(),
                seasonService.toSeasonData(division.getSeason()));
    }
}
