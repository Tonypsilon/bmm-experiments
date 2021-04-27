package de.berlinerschachverband.bmm.seasons.service;

import de.berlinerschachverband.bmm.seasons.data.Season;
import de.berlinerschachverband.bmm.seasons.data.SeasonData;
import de.berlinerschachverband.bmm.seasons.data.SeasonRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class SeasonsService {

    private SeasonRepository seasonRepository;

    public SeasonsService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public List<SeasonData> getAllSeasons() {
        return List.of(new SeasonData("Season 1"), new SeasonData("Season 2"));
    }

    public SeasonData getSeason(String name) {
        return new SeasonData(seasonRepository.findByName(name).get().getName());
    }

    public void createSeason(String name) {
        Season season = new Season();
        season.setName(name);
        seasonRepository.saveAndFlush(season);
    }

}
