package de.berlinerschachverband.bmm.navigation.controller;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.navigation.data.AdministrationButtonData;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import de.berlinerschachverband.bmm.navigation.service.AdministrationService;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministrationController.class)
@AutoConfigureTestDatabase
class AdministrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private ClubService clubService;

    @MockBean
    private AdministrationService administrationService;

    @MockBean
    private ClubAdminService clubAdminService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
        when(administrationService.getAdministrationButtonData("user", List.of(Roles.ADMINISTRATOR)))
                .thenReturn(List.of(
                        new AdministrationButtonData("/administration/createSeason", "Neue Saison erstellen"),
                        new AdministrationButtonData("/administration/createDivision", "Neue Staffel erstellen"),
                        new AdministrationButtonData("/clubs", "Alle Vereine anzeigen"),
                        new AdministrationButtonData("/clubs/create", "Neuen Verein erstellen")
                ));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void shouldReturnAdminPage() throws Exception {
        this.mockMvc.perform(get("/administration"))
                .andExpect(status().isOk())
                .andExpect(view().name("administration"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("administrationButtons", List.of(
                        new AdministrationButtonData("/administration/createSeason", "Neue Saison erstellen"),
                        new AdministrationButtonData("/administration/createDivision", "Neue Staffel erstellen"),
                        new AdministrationButtonData("/clubs", "Alle Vereine anzeigen"),
                        new AdministrationButtonData("/clubs/create", "Neuen Verein erstellen")
                )));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = Roles.CLUB_ADMIN)
    void shouldReturnClubAdminPageWhenClubMatches() throws Exception {
        Club club1 = new Club();
        club1.setId(1L);
        club1.setName("club1");
        club1.setActive(true);
        when(clubService.getClub("club1")).thenReturn(club1);
        when(clubService.toClubData(club1)).thenReturn(new ClubData(1L, "club1", true, 1));

        when(clubAdminService.findClubsByUsername("testuser")).thenReturn(List.of(new ClubData(1L, "club1", true, 1)));

        this.mockMvc.perform(get("/administration/club/club1"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubAdministration"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("club", new ClubData(1L, "club1", true, 1)));

        this.mockMvc.perform(get("/administration/club/otherClub"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = Roles.USER)
    void shouldDenyAccessGetAdmin() throws Exception {
        this.mockMvc.perform(get("/administration"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {Roles.USER, Roles.TEAM_ADMIN, Roles.ADMINISTRATOR})
    void shouldDenyAccessGetClubAdmin() throws Exception {
        when(clubAdminService.findClubsByUsername("testuser")).thenReturn(List.of(new ClubData(1L,"myClub", true, 1)));
        this.mockMvc.perform(get("/administration/club/anyClub"))
                .andExpect(status().isForbidden());
    }

}