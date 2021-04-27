package de.berlinerschachverband.bmm.seasons.facade;

import de.berlinerschachverband.bmm.seasons.service.SeasonsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return "seasons";
    }

    @GetMapping(value = "/season")
    public String getSeason(@RequestParam final String name, final Model model) {
        model.addAttribute("season", seasonsService.getSeason(name));
        return "season";
    }

    @PostMapping("/createSeason")
    public String createSeason(@RequestParam final String name) {

        seasonsService.createSeason(name);
        return "redirect:/seasons";
    }
}
