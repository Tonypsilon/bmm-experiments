package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateSeasonData;
import de.berlinerschachverband.bmm.basedata.service.DivisionService;
import de.berlinerschachverband.bmm.basedata.service.SeasonService;
import de.berlinerschachverband.bmm.navigation.NavbarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SeasonController {

    private final SeasonService seasonService;
    private final NavbarService navbarService;
    private final DivisionService divisionService;

    public SeasonController(SeasonService seasonService,
                            NavbarService navbarService,
                            DivisionService divisionService) {
        this.seasonService = seasonService;
        this.navbarService = navbarService;
        this.divisionService = divisionService;
    }

    @GetMapping(value = "/season/{seasonName}")
    public String getSeason(@PathVariable final String seasonName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("season", seasonService.getSeason(seasonName));
        model.addAttribute("divisions", divisionService.getDivisionsOfSeasonByLevel(seasonName));
        System.out.println(divisionService.getDivisionsOfSeasonByLevel(seasonName).keySet());
        return "season";
    }

    @GetMapping(value = "/administration/createSeason")
    public String createSeason(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createSeasonData", new CreateSeasonData());
        return "createSeason";
    }

    @PostMapping(value = "/administration/createSeason")
    public String createSeason(@ModelAttribute CreateSeasonData createSeasonData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            model.addAttribute("season", seasonService.createSeason(createSeasonData.getSeasonName()));
            model.addAttribute("state", "success");
        } catch(Exception ex) {
            model.addAttribute("state", "failure");
        }
            return "seasonCreated";
    }
}
