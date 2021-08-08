package de.berlinerschachverband.bmm.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final NavbarService navbarService;

    public IndexController(NavbarService navbarService) {
        this.navbarService = navbarService;
    }

    @GetMapping(value = {"/", "/index.html"})
    public String toHome() {
        return "redirect:/home";
    }

    @GetMapping(value = "/home")
    public String home(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "home";
    }
}
