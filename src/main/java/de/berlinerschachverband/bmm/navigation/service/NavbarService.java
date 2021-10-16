package de.berlinerschachverband.bmm.navigation.service;

import de.berlinerschachverband.bmm.basedata.service.SeasonService;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import org.springframework.stereotype.Service;

@Service
public class NavbarService {

    private SeasonService seasonService;

    public NavbarService(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    public NavbarData getNavbarData() {
        return new NavbarData(seasonService.getSeasonNames());
    }
}
