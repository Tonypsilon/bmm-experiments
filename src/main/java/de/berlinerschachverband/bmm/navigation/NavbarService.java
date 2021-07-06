package de.berlinerschachverband.bmm.navigation;

import de.berlinerschachverband.bmm.basedata.service.SeasonService;
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
