package de.berlinerschachverband.bmm.poc.seasons.facade;

import de.berlinerschachverband.bmm.poc.seasons.service.SeasonsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SeasonsController {

    private final SeasonsService seasonsService;

    public SeasonsController(SeasonsService seasonsService) {
        this.seasonsService = seasonsService;
    }

    @GetMapping(value = "/seasons")
    public String getSeasons(final Model model
    ) {
        model.addAttribute("seasons", seasonsService.getAllSeasons());
        return "poc/seasons";
    }

    @GetMapping(value = "/season/{seasonName}")
    public String getSeason(@PathVariable final String seasonName, final Model model) {
        model.addAttribute("season", seasonsService.getSeason(seasonName));
        return "poc/season";
    }

    @GetMapping("/createSeason")
    public String createSeason(@RequestParam final String name) {

        seasonsService.createSeason(name);
        return "redirect:/poc/seasons";
    }
}
