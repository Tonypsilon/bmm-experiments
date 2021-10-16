package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateDivisionData;
import de.berlinerschachverband.bmm.basedata.service.DivisionService;
import de.berlinerschachverband.bmm.exceptions.DivisionAlreadyExistsException;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DivisionController {

    private final NavbarService navbarService;
    private final DivisionService divisionService;

    public DivisionController(NavbarService navbarService, DivisionService divisionService) {
        this.navbarService = navbarService;
        this.divisionService = divisionService;
    }

    @GetMapping(value="/administration/createDivision")
    public String createDivision(final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        model.addAttribute("createDivisionData", new CreateDivisionData());
        return "createDivision";
    }

    @PostMapping(value = "administration/createDivision")
    public String createDivision(@ModelAttribute CreateDivisionData createDivisionData, final Model model) {
        model.addAttribute("navbarData", navbarService.getNavbarData());
        try {
            model.addAttribute("division", divisionService.createDivision(createDivisionData));
            model.addAttribute("state", "success");
        } catch(DivisionAlreadyExistsException ex) {
            model.addAttribute("state", "failure");
        }
        return "divisionCreated";
    }
}
