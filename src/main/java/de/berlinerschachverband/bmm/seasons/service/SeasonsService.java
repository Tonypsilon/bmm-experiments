package de.berlinerschachverband.bmm.seasons.service;

import de.berlinerschachverband.bmm.seasons.data.Season;
import de.berlinerschachverband.bmm.seasons.data.SeasonRepository;
import org.springframework.stereotype.Service;

@Service
public class SeasonsService {

    private SeasonRepository seasonRepository;

    public SeasonsService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public String getAllSeasons() {
        return "all seasons";
    }

    public String getSeason(String name) {
        return "only season " + seasonRepository.findByName(name).get().getName();
    }

    public void createSeason(String name) {
        Season season = new Season();
        season.setName(name);
        seasonRepository.saveAndFlush(season);
    }

}
