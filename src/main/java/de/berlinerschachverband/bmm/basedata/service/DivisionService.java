package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.berlinerschachverband.bmm.basedata.data.Division;
import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.DivisionRepository;
import org.springframework.stereotype.Service;

@Service
public class DivisionService {

    private final DivisionRepository divisionRepository;

    private final SeasonService seasonService;

    public DivisionService(DivisionRepository divisionRepository, SeasonService seasonService) {
        this.divisionRepository = divisionRepository;
        this.seasonService = seasonService;
    }

    public Multimap<Integer, String> getDivisionsOfSeasonByLevel(String seasonName) {
        Multimap<Integer, String> divisionsByLevel = ArrayListMultimap.create();
        for(Division division : divisionRepository.findBySeason_Name(seasonName)) {
            divisionsByLevel.put(division.getLevel(), division.getName());
        }
        return divisionsByLevel;
    }

    public DivisionData toDivisionData(Division division) {
        return new DivisionData(division.getId(),
                division.getName(),
                division.getLevel(),
                seasonService.toSeasonData(division.getSeason()));
    }
}
