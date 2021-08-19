package de.berlinerschachverband.bmm.basedata.controller;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.service.DivisionService;
import de.berlinerschachverband.bmm.basedata.service.SeasonService;
import de.berlinerschachverband.bmm.navigation.NavbarData;
import de.berlinerschachverband.bmm.navigation.NavbarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeasonController.class)
class SeasonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeasonService seasonService;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private DivisionService divisionService;

    @Test
    public void getSeasonShouldReturnSeason() throws Exception {
        Multimap<Integer, String> divisions = ArrayListMultimap.create();
        divisions.put(1, "division1");
        divisions.put(2, "division2a");
        divisions.put(2, "division2b");
        when(seasonService.getSeason("testSeason")).thenReturn(new SeasonData(1L, "testSeason"));
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
        when(divisionService.getDivisionsOfSeasonByLevel("testSeason")).thenReturn(divisions);

        this.mockMvc.perform(get("/season/testSeason"))
                .andExpect(status().isOk())
                .andExpect(view().name("season"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("season", new SeasonData(1L, "testSeason")))
                .andExpect(model().attribute("divisions", divisions));
    }
}