package de.berlinerschachverband.bmm.poc.seasons.web;

import de.berlinerschachverband.bmm.poc.seasons.service.PocSeasonsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PocIndexController {

    private final PocSeasonsService pocSeasonsService;

    public PocIndexController(PocSeasonsService pocSeasonsService) {
        this.pocSeasonsService = pocSeasonsService;
    }

    @GetMapping(value = {"/poc", "/poc/index.html"})
    public String toHome() {
        return "redirect:/poc/home";
    }

    @GetMapping(value = "/poc/home")
    public String home(final Model model) {
        model.addAttribute("seasons", pocSeasonsService.getAllSeasons());
        return "poc/home";
    }
}
