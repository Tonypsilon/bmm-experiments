package de.berlinerschachverband.bmm.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministrationController {

    private final NavbarService navbarService;

    public AdministrationController(NavbarService navbarService) {
        this.navbarService = navbarService;
    }

    @GetMapping(value = "/administration")
    public String admin(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "administration";
    }
}
