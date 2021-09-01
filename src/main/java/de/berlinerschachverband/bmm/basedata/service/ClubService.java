package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

    private final SeasonService seasonService;

    public ClubService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    public ClubData toClubData(Club club) {
        return new ClubData(club.getId(),
                club.getName(),
                seasonService.toSeasonData(club.getSeason()));
    }
}
