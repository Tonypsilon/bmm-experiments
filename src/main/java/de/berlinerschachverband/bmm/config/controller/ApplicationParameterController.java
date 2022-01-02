package de.berlinerschachverband.bmm.config.controller;

import de.berlinerschachverband.bmm.config.data.thymeleaf.SetApplicationParameterData;
import de.berlinerschachverband.bmm.config.service.ApplicationParameterService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.security.RolesAllowed;

@Controller
public class ApplicationParameterController {

    private final NavbarService navbarService;
    private final ApplicationParameterService applicationParameterService;

    public ApplicationParameterController(NavbarService navbarService,
                                          ApplicationParameterService applicationParameterService) {
        this.navbarService = navbarService;
        this.applicationParameterService = applicationParameterService;
    }

    /**
     * For now not intended to be used.
     * @param model
     * @return
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    @GetMapping(value = "/config/parameter")
    private String setApplicationParameter(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("applicationParameterData", new SetApplicationParameterData());
        return "setApplicationParameter";
    }

    /**
     * For now not intended to be used.
     * @param setApplicationParameterData
     * @param model
     * @return
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    @PostMapping(value = "/config/parameter")
    private String setApplicationParameter(@ModelAttribute SetApplicationParameterData setApplicationParameterData,
                                          final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            applicationParameterService.setApplicationParameter(setApplicationParameterData);
            model.addAttribute("state", "success");
        } catch (Exception ex) {
            model.addAttribute("state", "failure");
        }
        return "applicationParameterSet";
    }
}
