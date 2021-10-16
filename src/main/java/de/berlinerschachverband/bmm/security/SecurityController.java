package de.berlinerschachverband.bmm.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping(value = "/accessDenied")
    public String getAccessDenied(final Model model) {
        return "accessDenied";
    }
}
