package de.berlinerschachverband.bmm.poc.playground;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PocPlaygroundController {

    @GetMapping(value = "/poc/playground")
    public String playground(final Model model) {
        model.addAttribute("regularTableData", createData());
        return "poc/playground";
    }

    private List<PocRegularTableData> createData() {
        List<PocRegularTableData> regularTableData = new ArrayList<>();
        regularTableData.add(new PocRegularTableData("1","Team 1", "10", "32,5"));
        regularTableData.add(new PocRegularTableData("2","Team 2", "8", "33,0"));
        regularTableData.add(new PocRegularTableData("3","Team 3", "6", "21,5"));
        regularTableData.add(new PocRegularTableData("4","Team 4", "2", "10,0"));
        return regularTableData;
    }
}
