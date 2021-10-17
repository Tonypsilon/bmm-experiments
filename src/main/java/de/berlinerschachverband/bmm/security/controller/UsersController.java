package de.berlinerschachverband.bmm.security.controller;

import de.berlinerschachverband.bmm.exceptions.UserAlreadyExistsException;
import de.berlinerschachverband.bmm.navigation.data.CreateUserData;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.UsersService;
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

    @RolesAllowed(Roles.administrator)
    @GetMapping("/administration/createUser")
    public String createUser(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createUserData", new CreateUserData());
        return "createUser";
    }

    @RolesAllowed(Roles.administrator)
    @PostMapping("/administration/createUser")
    public String createUser(@ModelAttribute CreateUserData createUserData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        if(createUserData.getPassword().isBlank()) {
            model.addAttribute("errorMessage", "Das Passwort darf nicht leer sein.");
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

}
