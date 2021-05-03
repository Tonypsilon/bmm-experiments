package de.berlinerschachverband.bmm.poc.seasons.web;

import de.berlinerschachverband.bmm.poc.seasons.service.SeasonsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final SeasonsService seasonsService;

    public IndexController(SeasonsService seasonsService) {
        this.seasonsService = seasonsService;
    }

    @GetMapping(value = {"/", "/index.html"})
    public String toHome() {
        return "redirect:/poc/home";
    }

    @GetMapping(value = "/home")
    public String home(final Model model) {
        model.addAttribute("seasons", seasonsService.getAllSeasons());
        return "poc/home";
    }
}