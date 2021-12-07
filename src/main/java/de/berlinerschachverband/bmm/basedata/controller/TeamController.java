package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.EditTeamData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.TeamService;
import de.berlinerschachverband.bmm.basedata.service.TeamValidationService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
import org.springframework.context.annotation.Role;
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
    private final TeamValidationService teamValidationService;
    private final ClubAdminService clubAdminService;

    public TeamController(NavbarService navbarService,
                          TeamService teamService,
                          ClubService clubService,
                          TeamValidationService teamValidationService,
                          ClubAdminService clubAdminService) {
        this.navbarService = navbarService;
        this.teamService = teamService;
        this.clubService = clubService;
        this.teamValidationService = teamValidationService;
        this.clubAdminService = clubAdminService;
    }

    @RolesAllowed({Roles.CLUB_ADMIN})
    @GetMapping(value = "club/{clubName}/teams")
    public String getTeamsOfClub(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        model.addAttribute("club", clubService.getClub(clubName));
        model.addAttribute("teams", teamValidationService.getTeamsOfClubValidated(clubName));
        return "teamsOfClub";
    }

    @RolesAllowed({Roles.CLUB_ADMIN})
    @GetMapping(value = "club/{clubName}/teams/{teamNumber}/edit")
    public String editTeamOfClub(@PathVariable final String clubName,
                                 @PathVariable final Integer teamNumber,
                                 final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        model.addAttribute("editTeamData", new EditTeamData());//teamService.getTeamForEditing(clubName, teamNumber));
        return "editTeam";
    }

    @RolesAllowed({Roles.CLUB_ADMIN})
    @PostMapping(value ="club/{clubName}/teams/{teamNumber}/edit")
    public String editTeamOfClub(@PathVariable final String clubName,
                                 @PathVariable final Integer teamNumber,
                                 @ModelAttribute EditTeamData editTeamData,
                                 final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        return "editedTeam";
    }
}
