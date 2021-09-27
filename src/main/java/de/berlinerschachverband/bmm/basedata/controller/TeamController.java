package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.TeamService;
import de.berlinerschachverband.bmm.navigation.NavbarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TeamController {

    private final NavbarService navbarService;
    private final TeamService teamService;
    private final ClubService clubService;

    public TeamController(NavbarService navbarService,
                          TeamService teamService,
                          ClubService clubService) {
        this.navbarService = navbarService;
        this.teamService = teamService;
        this.clubService = clubService;
    }

    @GetMapping(value = "/teams/createTeam")
    public String createTeam(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createTeamData", new CreateTeamData());
        model.addAttribute("clubs", clubService.getAllActiveClubsNames());
        return "createTeam";
    }

    @GetMapping(value = "/teams/createTeams")
    public String createTeams(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createTeamsData", new CreateTeamsData());
        model.addAttribute("clubs", clubService.getAllActiveClubsNames());
        return "createTeams";
    }

    @PostMapping(value = "/teams/createTeam")
    public String createTeam(@ModelAttribute CreateTeamData createTeamData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            // create team
        } catch (Exception ex) {
            // TODO
        }
        return "teamCreated";
    }
}
