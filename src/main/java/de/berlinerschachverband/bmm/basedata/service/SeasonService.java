package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Season;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.SeasonRepository;
import de.berlinerschachverband.bmm.exceptions.SeasonAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.SeasonNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;

    public SeasonService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    /**
     * Get all seasons, ordered alphabetically by their name.
     * @return
     */
    public List<SeasonData> getAllSeasons() {
        return seasonRepository.findAll().stream()
                .sorted(Comparator.comparing(Season::getName))
                .map(this::toSeasonData)
                .toList();
    }

    /**
     * Get all season names, ordered alphabetically.
     * @return
     */
    public List<String> getSeasonNames() {
        return seasonRepository.findAll().stream()
                .map(Season::getName)
                .sorted(String::compareTo)
                .toList();
    }

    /**
     * Get a season, given its name. If not found, a SeasonNotFoundException is thrown.
     * @param name
     * @return
     */
    public Season getSeason(String name) {
        return seasonRepository.findByName(name)
                .orElseThrow(() -> new SeasonNotFoundException(name));
    }

    /**
     * Create a season by name. If a season with that name already exists,
     * a SeasonAlreadyExistsException is thrown.
     * @param seasonName
     * @return
     */
    public SeasonData createSeason(String seasonName) {
        if(seasonRepository.findByName(seasonName).isPresent()) {
            throw new SeasonAlreadyExistsException(seasonName);
        }
        Season season = new Season();
        season.setName(seasonName);
        seasonRepository.saveAndFlush(season);
        return toSeasonData(getSeason(seasonName));
    }

    public SeasonData toSeasonData(Season season) {
        return new SeasonData(season.getId(), season.getName());
    }

}
