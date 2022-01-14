package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PrepareEditTeamData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.EditTeamService;
import de.berlinerschachverband.bmm.basedata.service.TeamDataAccessService;
import de.berlinerschachverband.bmm.basedata.service.TeamValidationService;
import de.berlinerschachverband.bmm.config.service.ApplicationParameterService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
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
    private final TeamDataAccessService teamDataAccessService;
    private final ClubService clubService;
    private final TeamValidationService teamValidationService;
    private final EditTeamService editTeamService;
    private final ClubAdminService clubAdminService;
    private final ApplicationParameterService applicationParameterService;

    public TeamController(NavbarService navbarService,
                          TeamDataAccessService teamDataAccessService,
                          ClubService clubService,
                          TeamValidationService teamValidationService,
                          EditTeamService editTeamService,
                          ClubAdminService clubAdminService,
                          ApplicationParameterService applicationParameterService) {
        this.navbarService = navbarService;
        this.teamDataAccessService = teamDataAccessService;
        this.clubService = clubService;
        this.teamValidationService = teamValidationService;
        this.editTeamService = editTeamService;
        this.clubAdminService = clubAdminService;
        this.applicationParameterService = applicationParameterService;
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
        if(!"teamCreation".equals(applicationParameterService.getApplicationParameter("applicationStage").orElse(""))) {
            return "wrongApplicationStage";
        }
        model.addAttribute("prepareEditTeamData", editTeamService.getTeamForEditing(clubName, teamNumber));
        return "editTeam";
    }

    @RolesAllowed({Roles.CLUB_ADMIN})
    @PostMapping(value ="club/{clubName}/teams/{teamNumber}/edit")
    public String editTeamOfClub(@PathVariable final String clubName,
                                 @PathVariable final Integer teamNumber,
                                 @ModelAttribute PrepareEditTeamData prepareEditTeamData,
                                 final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        if(!"teamCreation".equals(applicationParameterService.getApplicationParameter("applicationStage").orElse(""))) {
            return "wrongApplicationStage";
        }
        editTeamService.editTeam(prepareEditTeamData);
        return "editedTeam";
    }
}
