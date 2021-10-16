package de.berlinerschachverband.bmm.navigation.controller;

import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.navigation.data.AdministrationButtonData;
import de.berlinerschachverband.bmm.navigation.service.AdministrationService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;
import java.util.List;

@Controller
public class AdministrationController {

    private final NavbarService navbarService;
    private final ClubService clubService;
    private final AdministrationService administrationService;

    public AdministrationController(NavbarService navbarService,
                                    ClubService clubService,
                                    AdministrationService administrationService) {
        this.navbarService = navbarService;
        this.clubService = clubService;
        this.administrationService = administrationService;
    }

    @RolesAllowed({Roles.administrator, Roles.clubAdmin, Roles.teamAdmin})
    @GetMapping(value = "/administration")
    public String admin(final Model model) {
        List<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("administrationButtons", administrationService.getAdministrationButtonData(roles));
        return "administration";
    }

    @GetMapping(value = "/administration/club/{clubName}")
    public String clubAdmin(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("club", clubService.toClubData(clubService.getClub(clubName)));
        return "clubAdministration";
    }
}
