package de.berlinerschachverband.bmm.security.controller;

import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.exceptions.UserAlreadyExistsException;
import de.berlinerschachverband.bmm.security.data.ChangePasswordData;
import de.berlinerschachverband.bmm.security.data.CreateUserData;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.UsersService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.security.RolesAllowed;

@Controller
public class UsersController {

    private final NavbarService navbarService;
    private final UsersService usersService;

    public UsersController(NavbarService navbarService, UsersService usersService) {
        this.navbarService = navbarService;
        this.usersService = usersService;
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @GetMapping("/administration/createUser")
    public String createUser(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createUserData", new CreateUserData());
        return "createUser";
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @PostMapping("/administration/createUser")
    public String createUser(@ModelAttribute CreateUserData createUserData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        if(createUserData.getPassword().isBlank()) {
            model.addAttribute("errorMessage", "Das Passwort darf nicht leer sein.");
            return "createUser";
        }
        if(createUserData.getUsername().isBlank()) {
            model.addAttribute("errorMessage", "Der Benutzername darf nicht leer sein.");
            return "createUser";
        }
        if(!createUserData.getPassword().equals(createUserData.getPasswordConfirm())) {
            model.addAttribute("errorMessage", "Die Passwörter stimmen nicht überein.");
            return "createUser";
        }
        model.addAttribute("username", createUserData.getUsername());
        try {
            usersService.createUser(createUserData);
            model.addAttribute("state", "success");
        } catch (UserAlreadyExistsException ex) {
            model.addAttribute("state", "failure");
        }
        return "userCreated";
    }

    @RolesAllowed({Roles.USER, Roles.ADMINISTRATOR, Roles.TEAM_ADMIN, Roles.CLUB_ADMIN})
    @GetMapping("/administration/changePassword")
    public String changePassword(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("changePasswordData", new ChangePasswordData());
        return "changePassword";
    }

    @RolesAllowed({Roles.USER, Roles.ADMINISTRATOR, Roles.TEAM_ADMIN, Roles.CLUB_ADMIN})
    @PostMapping("/administration/changePassword")
    public String changePassword(@ModelAttribute ChangePasswordData changePasswordData,
                                 final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        if(Boolean.TRUE.equals(changePasswordData.getNewPassword().isBlank())) {
            model.addAttribute("errorMessage", "Das Passwort darf nicht leer sein.");
            return "changePassword";
        }
        if(Boolean.FALSE.equals(changePasswordData.getNewPassword().equals(changePasswordData.getNewPasswordConfirm()))) {
            model.addAttribute("errorMessage", "Die Passwörter stimmen nicht überein.");
            return "changePassword";
        }
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            usersService.changePassword(username,changePasswordData);
            model.addAttribute("state", "success");
        } catch (BmmException ex) {
            model.addAttribute("state", "failure");
        }
        return "passwordChanged";
    }

}
