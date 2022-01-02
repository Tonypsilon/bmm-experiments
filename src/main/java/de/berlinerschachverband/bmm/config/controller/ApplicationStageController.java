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
import java.util.Arrays;

@Controller
public class ApplicationStageController {

    private final NavbarService navbarService;
    private final ApplicationParameterService applicationParameterService;
    private final String[] stages = {"teamCreation", "seasonCreation", "seasonRunning"};

    public ApplicationStageController(NavbarService navbarService,
                                      ApplicationParameterService applicationParameterService) {
        this.navbarService = navbarService;
        this.applicationParameterService = applicationParameterService;
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @GetMapping(value = "/config/applicationStage")
    public String setApplicationStage(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        return "setApplicationStage";
    }

    @RolesAllowed(Roles.ADMINISTRATOR)
    @PostMapping(value = "config/applicationStage")
    public String setApplicationStage(@ModelAttribute String stage, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        if (Arrays.stream(stages).anyMatch(stage::equals)) {
            SetApplicationParameterData setApplicationParameterData = new SetApplicationParameterData();
            setApplicationParameterData.setKey("applicationStage");
            setApplicationParameterData.setValue(stage);
            applicationParameterService.setApplicationParameter(setApplicationParameterData);
            model.addAttribute("state", "success");
        } else {
            model.addAttribute("state", "failure");
        }
        return "applicationStageSet";
    }
}
