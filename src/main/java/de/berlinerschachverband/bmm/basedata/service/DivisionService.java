package de.berlinerschachverband.bmm.basedata.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.berlinerschachverband.bmm.basedata.data.Division;
import de.berlinerschachverband.bmm.basedata.data.DivisionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DivisionService {

    private final DivisionRepository divisionRepository;

    public DivisionService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    public Multimap<Integer, String> getDivisionsOfSeasonByLevel(String seasonName) {
        Multimap<Integer, String> divisionsByLevel = ArrayListMultimap.create();
        for(Division division : divisionRepository.findBySeason_Name(seasonName)) {
            divisionsByLevel.put(division.getLevel(), division.getName());
        }
        return divisionsByLevel;
    }
}
