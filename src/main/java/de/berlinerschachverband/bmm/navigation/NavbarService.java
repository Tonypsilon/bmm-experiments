package de.berlinerschachverband.bmm.navigation;

import de.berlinerschachverband.bmm.basedata.service.SeasonsService;
import org.springframework.stereotype.Service;

@Service
public class NavbarService {

    private SeasonsService seasonsService;

    public NavbarService(SeasonsService seasonsService) {
        this.seasonsService = seasonsService;
    }

    public NavbarData getNavbarData() {
        return new NavbarData(seasonsService.getSeasonNames());
    }
}
