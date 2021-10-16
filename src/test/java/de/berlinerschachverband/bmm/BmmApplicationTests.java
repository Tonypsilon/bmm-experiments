package de.berlinerschachverband.bmm;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateDivisionData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateSeasonData;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest()
@AutoConfigureMockMvc
class BmmApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void bmmSystemTest() throws Exception {
        CreateSeasonData createSeasonData = new CreateSeasonData();
        createSeasonData.setSeasonName("season1");

        // Step 1: Create a season.
        this.mockMvc.perform(post("/administration/createSeason")
                        .with(csrf())
                        .flashAttr("createSeasonData", createSeasonData))
                .andExpect(status().isOk())
                .andExpect(view().name("seasonCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "success"))
                .andExpect(model().attribute("season", new SeasonData(1L, "season1")));

        // Step 2: Create another season.
        createSeasonData.setSeasonName("season2");
        this.mockMvc.perform(post("/administration/createSeason")
                        .with(csrf())
                        .flashAttr("createSeasonData", createSeasonData))
                .andExpect(status().isOk())
                .andExpect(view().name("seasonCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "success"))
                .andExpect(model().attribute("season", new SeasonData(2L, "season2")));

        // Step 3: Check the home page.
        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("season1", "season2"))));

        // Step 4: Create a division for season1.
        CreateDivisionData createDivisionData = new CreateDivisionData();
        createDivisionData.setName("division1");
        createDivisionData.setLevel(1);
        createDivisionData.setSeasonName("season1");

        this.mockMvc.perform(post("/administration/createDivision")
                        .with(csrf())
                        .flashAttr("createDivisionData", createDivisionData))
                .andExpect(status().isOk())
                .andExpect(view().name("divisionCreated"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("season1", "season2"))))
                .andExpect(model().attribute("state", "success"))
                .andExpect(model().attribute("division", new DivisionData(1L, "division1", 1, new SeasonData(1L,"season1"))));

        // Step 5: Check season1 for division.
        SortedSetMultimap<Integer, String> divisions = TreeMultimap.create();
        divisions.put(1, "division1");

        this.mockMvc.perform(get("/season/season1"))
                .andExpect(status().isOk())
                .andExpect(view().name("season"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("season1", "season2"))))
                .andExpect(model().attribute("season", new SeasonData(1L, "season1")))
                .andExpect(model().attribute("divisions", divisions));

        // Step 6: Create a club.

    }

}
