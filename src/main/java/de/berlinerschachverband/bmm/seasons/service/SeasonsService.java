package de.berlinerschachverband.bmm.seasons.service;

import de.berlinerschachverband.bmm.seasons.data.Season;
import de.berlinerschachverband.bmm.seasons.data.SeasonData;
import de.berlinerschachverband.bmm.seasons.data.SeasonRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeasonsService {

    private SeasonRepository seasonRepository;

    public SeasonsService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public List<SeasonData> getAllSeasons() {
        return seasonRepository.findAll().stream().map(this::toSeasonData).collect(Collectors.toList());
    }

    public SeasonData getSeason(String name) {
        return new SeasonData(seasonRepository.findByName(name).get().getName());
    }

    public void createSeason(String name) {
        Season season = new Season();
        season.setName(name);
        seasonRepository.saveAndFlush(season);
    }

    private SeasonData toSeasonData(Season season) {
        SeasonData seasonData = new SeasonData(season.getName());
        return seasonData;
    }
}
