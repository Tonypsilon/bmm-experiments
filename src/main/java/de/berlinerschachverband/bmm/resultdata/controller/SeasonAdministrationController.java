package de.berlinerschachverband.bmm.resultdata.controller;

import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.service.SeasonService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.resultdata.service.MatchDayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SeasonAdministrationController {

    private final NavbarService navbarService;
    private final MatchDayService matchDayService;
    private final SeasonService seasonService;

    public SeasonAdministrationController(NavbarService navbarService,
                                          MatchDayService matchDayService,
                                          SeasonService seasonService) {
        this.navbarService = navbarService;
        this.matchDayService = matchDayService;
        this.seasonService = seasonService;
    }

    @GetMapping(value = "/administration/season/{seasonName}")
    public String getSeasonAdministrationOverview(@PathVariable  final String seasonName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "seasonAdministration";
    }

    @PostMapping(value = "administration/season/{seasonName}/createMatchDays")
    public String createMatchDaysForSeason(@PathVariable final String seasonName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        SeasonData seasonData = seasonService.toSeasonData(seasonService.getSeason(seasonName));
        matchDayService.createRoundRobinMatchDaysForSeason(seasonData);
        return "matchDaysCreated";
    }
}
