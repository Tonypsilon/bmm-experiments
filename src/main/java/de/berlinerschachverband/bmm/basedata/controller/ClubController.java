package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.exceptions.ClubNotFoundException;
import de.berlinerschachverband.bmm.navigation.NavbarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final NavbarService navbarService;

    public ClubController(ClubService clubService, NavbarService navbarService) {
        this.clubService = clubService;
        this.navbarService = navbarService;
    }

    @GetMapping(value = "/clubs")
    public String getClubs(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("clubs", clubService.getAllClubs());
        return "clubs";
    }

    @GetMapping(value = "/clubs/active")
    public String getActiveClubs(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("clubs", clubService.getAllActiveClubs());
        return "activeClubs";
    }

    @PostMapping(value = "/club/deactivate/{clubName}")
    public String deactivateClub(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("clubName", clubName);
        model.addAttribute("type", "deactivate");
        try {
            clubService.deactivateClub(clubName);
            model.addAttribute("state", "success");
        } catch (ClubNotFoundException ex) {
            model.addAttribute("state", "failure");
        }
        return "clubActivityChanged";
    }

    @PostMapping(value = "/club/activate/{clubName}")
    public String activateClub(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("clubName", clubName);
        model.addAttribute("type", "activate");
        try {
            clubService.activateClub(clubName);
            model.addAttribute("state", "success");
        } catch (ClubNotFoundException ex) {
            model.addAttribute("state", "failure");
        }
        return "clubActivityChanged";
    }
}
