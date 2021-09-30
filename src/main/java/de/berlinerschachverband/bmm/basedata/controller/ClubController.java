package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.exceptions.ClubAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.ClubNotFoundException;
import de.berlinerschachverband.bmm.exceptions.NameBlankException;
import de.berlinerschachverband.bmm.navigation.NavbarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/club/create")
    public String createClub(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createClubData", new CreateClubData());
        return "createClub";
    }

    @PostMapping(value = "/club/create")
    public String createClub(@ModelAttribute CreateClubData createClubData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            model.addAttribute("club", clubService.createClub(createClubData.getClubName()));
            model.addAttribute("state", "success");
        } catch (NameBlankException | ClubAlreadyExistsException ex ) {
            model.addAttribute("state", "failure");
        }
        return "clubCreated";
    }

}
