package de.berlinerschachverband.bmm.security.controller;

import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.data.thymeleaf.AddRoleData;
import de.berlinerschachverband.bmm.security.data.thymeleaf.RemoveRoleData;
import de.berlinerschachverband.bmm.security.service.RolesService;
import de.berlinerschachverband.bmm.security.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.security.RolesAllowed;

@Controller
public class RolesController {

    private final NavbarService navbarService;
    private final RolesService rolesService;
    private final UsersService usersService;

    public RolesController(final NavbarService navbarService,
                           final RolesService rolesService,
                           final UsersService usersService) {
        this.navbarService = navbarService;
        this.rolesService = rolesService;
        this.usersService = usersService;
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @GetMapping("/roles/addUserRole")
    public String addUserRole(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("addRoleData", new AddRoleData());
        model.addAttribute("usernames", usersService.getAllUserNames());
        model.addAttribute("roles", rolesService.getAllNonAdministratorRoleNaturalNames());
        return "addUserRole";
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @PostMapping("/roles/addUserRole")
    public String addUserRole(@ModelAttribute AddRoleData addRoleData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            rolesService.addUserRole(addRoleData);
            model.addAttribute("state", "success");
        } catch (BmmException ex) {
            model.addAttribute("state", "failure");
        }
        return "roleAdded";
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @GetMapping("/roles/removeUserRole")
    public String removeUserRole(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("removeRoleData", new RemoveRoleData());
        model.addAttribute("usernames", usersService.getAllUserNames());
        model.addAttribute("roles", rolesService.getAllNonAdministratorRoleNaturalNames());
        return "removeUserRole";
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @PostMapping("/roles/removeUserRole")
    public String removeUserRole(@ModelAttribute RemoveRoleData removeRoleData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            rolesService.removeUserRole(removeRoleData);
            model.addAttribute("state", "success");
        } catch (BmmException ex) {
            model.addAttribute("state", "failure");
        }
        return "roleRemoved";
    }
}
