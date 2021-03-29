package de.berlinerschachverband.bmm.seasons.facade;

import de.berlinerschachverband.bmm.seasons.service.SeasonsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeasonsController {

    private final SeasonsService seasonsService;

    public SeasonsController(SeasonsService seasonsService) {
        this.seasonsService = seasonsService;
    }

    @GetMapping(value = "/seasons")
    public String getSeasons() {
        return seasonsService.getAllSeasons();
    }

    @GetMapping(value = "/season")
    public String getSeason(@RequestParam final String name) {
        return seasonsService.getSeason(name);
    }

    @PostMapping("/createSeason")
    public void createSeason(@RequestParam final String name) {
        seasonsService.createSeason(name);
    }
}
