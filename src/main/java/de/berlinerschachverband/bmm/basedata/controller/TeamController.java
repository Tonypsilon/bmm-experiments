package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.EditTeamData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.TeamService;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.security.RolesAllowed;

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

    @GetMapping(value = "club/{clubName}/teams")
    public String getTeamsOfClub(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("club", clubService.getClub(clubName));
        model.addAttribute("teams", teamService.getTeamsOfClub(clubName));
        return "teamsOfClub";
    }

    @GetMapping(value = "club/{clubName}/teams/{teamNumber}/edit")
    public String editTeamOfClub(@PathVariable final String clubName,
                                 @PathVariable final Integer teamNumber,
                                 final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "editTeam";
    }

    @PostMapping(value ="club/{clubName}/teams/{teamNumber}/edit")
    public String editTeamOfClub(@PathVariable final String clubName,
                                 @PathVariable final Integer teamNumber,
                                 @ModelAttribute EditTeamData editTeamData,
                                 final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "editedTeam";
    }
}
