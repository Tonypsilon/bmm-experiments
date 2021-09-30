package de.berlinerschachverband.bmm.navigation;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministrationController.class)
class AdministrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private ClubService clubService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }

    @Test
    void shouldReturnAdminPage() throws Exception {
        this.mockMvc.perform(get("/administration"))
                .andExpect(status().isOk())
                .andExpect(view().name("administration"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))));
    }

    @Test
    void shouldReturnClubAdminPage() throws Exception {
        Club club1 = new Club();
        club1.setId(1L);
        club1.setName("club1");
        club1.setActive(true);
        when(clubService.getClub("club1")).thenReturn(club1);
        when(clubService.toClubData(club1)).thenReturn(new ClubData(1L, "club1", true));

        this.mockMvc.perform(get("/administration/club/club1"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubAdministration"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("club", new ClubData(1L, "club1", true)));
    }
}