package de.berlinerschachverband.bmm.navigation;

import de.berlinerschachverband.bmm.basedata.service.ClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdministrationController {

    private final NavbarService navbarService;
    private final ClubService clubService;

    public AdministrationController(NavbarService navbarService, ClubService clubService) {
        this.navbarService = navbarService;
        this.clubService = clubService;
    }

    @GetMapping(value = "/administration")
    public String admin(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "administration";
    }

    @GetMapping(value = "/administration/club/{clubName}")
    public String clubAdmin(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("club", clubService.toClubData(clubService.getClub(clubName)));
        return "clubAdministration";
    }
}
