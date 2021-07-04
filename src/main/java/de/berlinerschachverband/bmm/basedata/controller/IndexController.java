package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.poc.seasons.service.PocSeasonsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final PocSeasonsService pocSeasonsService;

    public IndexController(PocSeasonsService pocSeasonsService) {
        this.pocSeasonsService = pocSeasonsService;
    }

    @GetMapping(value = {"/", "/index.html"})
    public String toHome() {
        return "redirect:/home";
    }

    @GetMapping(value = "/home")
    public String home(final Model model) {
        model.addAttribute("seasons", pocSeasonsService.getAllSeasons());
        return "home";
    }
}
