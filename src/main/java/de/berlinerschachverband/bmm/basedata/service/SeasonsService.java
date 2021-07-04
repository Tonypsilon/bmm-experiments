package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Season;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.SeasonRepository;
import org.springframework.stereotype.Service;

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

    public List<String> getSeasonNames() {
        return seasonRepository.findAll().stream().map(Season::getName).collect(Collectors.toList());
    }

    public SeasonData getSeason(String name) {
        return new SeasonData(seasonRepository.findByName(name).get().getName());
    }

    public void createSeason(SeasonData seasonData) {
        Season season = new Season();
        season.setName(seasonData.name());
        seasonRepository.saveAndFlush(season);
    }

    private SeasonData toSeasonData(Season season) {
        SeasonData seasonData = new SeasonData(season.getName());
        return seasonData;
    }
}
