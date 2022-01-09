package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateClubData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.RemoveTeamsData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.TeamCrudService;
import de.berlinerschachverband.bmm.config.service.ApplicationParameterService;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.exceptions.ClubAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.ClubNotFoundException;
import de.berlinerschachverband.bmm.exceptions.NameBlankException;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final ClubAdminService clubAdminService;
    private final NavbarService navbarService;
    private final TeamCrudService teamCrudService;
    private final ApplicationParameterService applicationParameterService;

    public ClubController(ClubService clubService,
                          NavbarService navbarService,
                          TeamCrudService teamCrudService,
                          ClubAdminService clubAdminService,
                          ApplicationParameterService applicationParameterService) {
        this.clubService = clubService;
        this.navbarService = navbarService;
        this.teamCrudService = teamCrudService;
        this.clubAdminService = clubAdminService;
        this.applicationParameterService = applicationParameterService;
    }

    @RolesAllowed({Roles.ADMINISTRATOR})
    @GetMapping(value = "/clubs")
    public String getClubs(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("clubs", clubService.getAllClubs());
        return "clubs";
    }

    @RolesAllowed({Roles.ADMINISTRATOR})
    @GetMapping(value = "/clubs/active")
    public String getActiveClubs(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("clubs", clubService.getAllActiveClubs());
        return "activeClubs";
    }

    @RolesAllowed({Roles.ADMINISTRATOR})
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

    @RolesAllowed({Roles.ADMINISTRATOR})
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

    @RolesAllowed(Roles.ADMINISTRATOR)
    @GetMapping(value = "/club/create")
    public String createClub(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createClubData", new CreateClubData());
        return "createClub";
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @PostMapping(value = "/club/create")
    public String createClub(@ModelAttribute CreateClubData createClubData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            model.addAttribute("club", clubService.createClub(createClubData.getClubName(), createClubData.getZps()));
            model.addAttribute("state", "success");
        } catch (NameBlankException | ClubAlreadyExistsException ex ) {
            model.addAttribute("state", "failure");
        }
        return "clubCreated";
    }

    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLUB_ADMIN})
    @GetMapping(value = "club/{clubName}/createTeams")
    public String createTeams(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        if(!"teamCreation".equals(applicationParameterService.getApplicationParameter("applicationStage").get())) {
            return "wrongApplicationStage";
        }
        model.addAttribute("createTeamsData", new CreateTeamsData());
        model.addAttribute("club", clubName);
        return "createTeams";
    }

    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLUB_ADMIN})
    @PostMapping(value = "club/{clubName}/createTeams")
    public String createTeams(@ModelAttribute CreateTeamsData createTeamsData,
                              @PathVariable final String clubName,
                              final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        if(!"teamCreation".equals(applicationParameterService.getApplicationParameter("applicationStage").orElse(""))) {
            return "wrongApplicationStage";
        }
        try {
            createTeamsData.setClubName(clubName);
            teamCrudService.createTeams(createTeamsData);
            model.addAttribute("state", "success");
        } catch (BmmException ex) {
            model.addAttribute("state", "failure");
        }
        return "teamsCreated";
    }

    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLUB_ADMIN})
    @GetMapping(value = "club/{clubName}/removeTeams")
    public String removeTeams(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        if(!"teamCreation".equals(applicationParameterService.getApplicationParameter("applicationStage").orElse(""))) {
            return "wrongApplicationStage";
        }
        model.addAttribute("removeTeamsData", new RemoveTeamsData());
        model.addAttribute("club", clubName);
        return "removeTeams";
    }

    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLUB_ADMIN})
    @PostMapping(value = "club/{clubName}/removeTeams")
    public String removeTeams(@ModelAttribute RemoveTeamsData removeTeamsData,
                              @PathVariable final String clubName,
                              final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        clubAdminService.validateClubAdminHasClubAccess(clubName);
        if(!"teamCreation".equals(applicationParameterService.getApplicationParameter("applicationStage").orElse(""))) {
            return "wrongApplicationStage";
        }
        try {
            removeTeamsData.setClubName(clubName);
            teamCrudService.removeTeams(removeTeamsData);
            model.addAttribute("state", "success");
        } catch (BmmException ex) {
            model.addAttribute("state", "failure");
        }
        return "teamsRemoved";
    }

}
