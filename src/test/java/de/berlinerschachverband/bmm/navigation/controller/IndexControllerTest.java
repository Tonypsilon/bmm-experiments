package de.berlinerschachverband.bmm.navigation.controller;

import de.berlinerschachverband.bmm.navigation.controller.IndexController;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
@AutoConfigureTestDatabase
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @Test
    void testRedirect() throws Exception{

        this.mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/home"));

        this.mockMvc.perform(get("/index.html"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    void testGetHome() throws Exception {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));

        this.mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))));
    }
}