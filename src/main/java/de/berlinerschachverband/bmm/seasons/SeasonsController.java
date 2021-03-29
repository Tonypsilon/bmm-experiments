package de.berlinerschachverband.bmm.seasons;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeasonsController {

    @GetMapping(value = "/seasons")
    public String getSeasons() {
        return "all seasons";
    }

    @GetMapping(value = "/season")
    public String getSeason(@RequestParam final String name) {
        return "only season " + name;
    }
}
