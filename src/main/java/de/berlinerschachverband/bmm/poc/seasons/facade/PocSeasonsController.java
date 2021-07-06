package de.berlinerschachverband.bmm.poc.seasons.facade;

import de.berlinerschachverband.bmm.poc.seasons.service.PocSeasonsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PocSeasonsController {

    private final PocSeasonsService pocSeasonsService;

    public PocSeasonsController(PocSeasonsService pocSeasonsService) {
        this.pocSeasonsService = pocSeasonsService;
    }

    @GetMapping(value = "/poc/seasons")
    public String getSeasons(final Model model
    ) {
        model.addAttribute("seasons", pocSeasonsService.getAllSeasons());
        return "poc/seasons";
    }

    @GetMapping(value = "/poc/season/{seasonName}")
    public String getSeason(@PathVariable final String seasonName, final Model model) {
        model.addAttribute("season", pocSeasonsService.getSeason(seasonName));
        return "poc/season";
    }

    @GetMapping("/poc/createSeason")
    public String createSeason(@RequestParam final String name) {

        pocSeasonsService.createSeason(name);
        return "redirect:/poc/seasons";
    }
}
