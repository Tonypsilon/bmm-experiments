package de.berlinerschachverband.bmm.poc.seasons.service;

import de.berlinerschachverband.bmm.basedata.data.Season;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.poc.seasons.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.SeasonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PocSeasonsService {

    private SeasonRepository seasonRepository;

    public PocSeasonsService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public List<SeasonData> getAllSeasons() {
        return seasonRepository.findAll().stream().map(this::toSeasonData).toList();
    }

    public SeasonData getSeason(String name) {
        return new SeasonData(seasonRepository.findByName(name)
                .orElseThrow(
                        ()->new BmmException("season with name " + name + " does not exist."))
                .getName());
    }

    public void createSeason(String name) {
        Season season = new Season();
        season.setName(name);
        seasonRepository.saveAndFlush(season);
    }

    private SeasonData toSeasonData(Season season) {
        return new SeasonData(season.getName());
    }
}
