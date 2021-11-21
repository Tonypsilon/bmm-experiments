package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateDivisionData;
import de.berlinerschachverband.bmm.basedata.service.DivisionService;
import de.berlinerschachverband.bmm.exceptions.DivisionAlreadyExistsException;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DivisionController.class)
@AutoConfigureTestDatabase
public class DivisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private DivisionService divisionService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }

    @Test
    @WithMockUser
    void testGetCreateDivision() throws Exception {
        this.mockMvc.perform(get("/administration/createDivision"))
                .andExpect(status().isOk())
                .andExpect(view().name("createDivision"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attributeExists("createDivisionData"));
    }

    @Test
    @WithMockUser
    void testPostCreateDivisionSuccess() throws Exception {
        CreateDivisionData createDivisionData = new CreateDivisionData();
        createDivisionData.setName("division1");
        createDivisionData.setLevel(1);
        createDivisionData.setNumberOfBoards(8);
        createDivisionData.setSeasonName("testSeason");
        when(divisionService.createDivision(createDivisionData))
                .thenReturn(new DivisionData(1L, "division1", 1, 8, new SeasonData(1L, "testSeason", false)));
        this.mockMvc.perform(post("/administration/createDivision")
                        .with(csrf())
                        .flashAttr("createDivisionData", createDivisionData))
                .andExpect(status().isOk())
                .andExpect(view().name("divisionCreated"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "success"))
                .andExpect(model().attribute("division", new DivisionData(1L, "division1", 1, 8, new SeasonData(1L,"testSeason", false))));
    }

    @Test
    @WithMockUser
    void testPostCreateDivisionFailure() throws Exception {
        CreateDivisionData createDivisionData = new CreateDivisionData();
        createDivisionData.setName("division1");
        createDivisionData.setLevel(1);
        createDivisionData.setNumberOfBoards(8);
        createDivisionData.setSeasonName("testSeason");
        when(divisionService.createDivision(createDivisionData))
                .thenThrow(new DivisionAlreadyExistsException("season: testSeason, division: division1"));
        this.mockMvc.perform(post("/administration/createDivision")
                        .with(csrf())
                        .flashAttr("createDivisionData", createDivisionData))
                .andExpect(status().isOk())
                .andExpect(view().name("divisionCreated"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "failure"))
                .andExpect(model().attributeDoesNotExist("division"));
    }
}



