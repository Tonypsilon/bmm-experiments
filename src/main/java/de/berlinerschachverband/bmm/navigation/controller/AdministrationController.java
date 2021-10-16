package de.berlinerschachverband.bmm.navigation.controller;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.navigation.data.AdministrationButtonData;
import de.berlinerschachverband.bmm.navigation.service.AdministrationService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdministrationController {

    private final NavbarService navbarService;
    private final ClubService clubService;
    private final AdministrationService administrationService;
    private final ClubAdminService clubAdminService;

    public AdministrationController(NavbarService navbarService,
                                    ClubService clubService,
                                    AdministrationService administrationService,
                                    ClubAdminService clubAdminService) {
        this.navbarService = navbarService;
        this.clubService = clubService;
        this.administrationService = administrationService;
        this.clubAdminService = clubAdminService;
    }

    @RolesAllowed({Roles.administrator, Roles.clubAdmin, Roles.teamAdmin})
    @GetMapping(value = "/administration")
    public String admin(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        List<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("administrationButtons", administrationService.getAdministrationButtonData(username, roles));
        return "administration";
    }

    @RolesAllowed({Roles.clubAdmin})
    @GetMapping(value = "/administration/club/{clubName}")
    public String clubAdmin(@PathVariable final String clubName, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!clubAdminService.findClubsByUsername(username)
                .stream()
                .map(ClubData::name)
                .toList()
                .contains(clubName)) {
            throw new AccessDeniedException(String.format("User %s is no clubAdmin for club %s", username, clubName));
        }
        model.addAttribute("club", clubService.toClubData(clubService.getClub(clubName)));
        return "clubAdministration";
    }
}
