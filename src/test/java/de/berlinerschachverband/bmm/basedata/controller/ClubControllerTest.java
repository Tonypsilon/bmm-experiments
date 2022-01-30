package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateClubData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateTeamsData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.RemoveTeamsData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.TeamCrudService;
import de.berlinerschachverband.bmm.config.service.ApplicationParameterService;
import de.berlinerschachverband.bmm.exceptions.BmmException;
import de.berlinerschachverband.bmm.exceptions.ClubAlreadyExistsException;
import de.berlinerschachverband.bmm.exceptions.ClubNotFoundException;
import de.berlinerschachverband.bmm.exceptions.NameBlankException;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubController.class)
@AutoConfigureTestDatabase
class ClubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private ClubService clubService;

    @MockBean
    private ClubAdminService clubAdminService;

    @MockBean
    private TeamCrudService teamCrudService;

    @MockBean
    private ApplicationParameterService applicationParameterService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testGetClubs() throws Exception {
        when(clubService.getAllClubs())
                .thenReturn(List.of(
                        new ClubData(1L, "club1", true, 1),
                        new ClubData(2L, "club2", false, 2)));
        this.mockMvc.perform(get("/clubs"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubs", List.of(
                        new ClubData(1L, "club1", true, 1),
                        new ClubData(2L, "club2", false, 2))));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testGetAllActiveClubs() throws Exception {
        when(clubService.getAllActiveClubs())
                .thenReturn(List.of(
                        new ClubData(1L, "club1", true, 1),
                        new ClubData(2L, "club2", true, 2)));
        this.mockMvc.perform(get("/clubs/active"))
                .andExpect(status().isOk())
                .andExpect(view().name("activeClubs"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubs", List.of(
                        new ClubData(1L, "club1", true, 1),
                        new ClubData(2L, "club2", true, 2))));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testDeactivateClub() throws Exception {
        this.mockMvc.perform(post("/club/deactivate/club1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("clubActivityChanged"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubName", "club1"))
                .andExpect(model().attribute("type", "deactivate"))
                .andExpect(model().attribute("state", "success"));
        verify(clubService, times(1)).deactivateClub("club1");
        doThrow(new ClubNotFoundException("club8")).when(clubService).deactivateClub("club8");
        this.mockMvc.perform(post("/club/deactivate/club8")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("clubActivityChanged"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubName", "club8"))
                .andExpect(model().attribute("type", "deactivate"))
                .andExpect(model().attribute("state", "failure"));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testActivateClub() throws Exception {
        this.mockMvc.perform(post("/club/activate/club1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("clubActivityChanged"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubName", "club1"))
                .andExpect(model().attribute("type", "activate"))
                .andExpect(model().attribute("state", "success"));
        verify(clubService, times(1)).activateClub("club1");
        doThrow(new ClubNotFoundException("club8")).when(clubService).activateClub("club8");
        this.mockMvc.perform(post("/club/activate/club8")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("clubActivityChanged"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubName", "club8"))
                .andExpect(model().attribute("type", "activate"))
                .andExpect(model().attribute("state", "failure"));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testGetCreateClub() throws Exception {
        this.mockMvc.perform(get("/club/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createClub"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attributeExists("createClubData"));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testPostCreateClubSuccess() throws Exception {
        CreateClubData createClubData = new CreateClubData();
        createClubData.setClubName("club1");
        createClubData.setZps(1);
        when(clubService.createClub("club1", 1)).thenReturn(new ClubData(1L, "club1", true, 1));
        when(applicationParameterService.getApplicationParameter("applicationStage")).thenReturn(Optional.of("teamCreation"));

        this.mockMvc.perform(post("/club/create")
                        .with(csrf())
                        .flashAttr("createClubData", createClubData))
                .andExpect(status().isOk())
                .andExpect(view().name("clubCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "success"))
                .andExpect(model().attribute("club", new ClubData(1L, "club1", true, 1)));

    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testPostCreateClubFailure() throws Exception {

        when(clubService.createClub("club8", 8)).thenThrow(new ClubAlreadyExistsException("club8"));
        when(clubService.createClub("",1)).thenThrow(new NameBlankException());
        when(clubService.createClub("club9", null)).thenThrow(new IllegalArgumentException("zps"));

        // club already exists
        CreateClubData createClubData = new CreateClubData();
        createClubData.setClubName("club8");
        createClubData.setZps(8);

        this.mockMvc.perform(post("/club/create")
                        .with(csrf())
                        .flashAttr("createClubData", createClubData))
                .andExpect(status().isOk())
                .andExpect(view().name("clubCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "failure"))
                .andExpect(model().attributeDoesNotExist("club"));

        // club name blank
        createClubData = new CreateClubData();
        createClubData.setClubName("");
        createClubData.setZps(1);

        this.mockMvc.perform(post("/club/create")
                        .with(csrf())
                        .flashAttr("createClubData", createClubData))
                .andExpect(status().isOk())
                .andExpect(view().name("clubCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "failure"))
                .andExpect(model().attributeDoesNotExist("club"));

        // zps null
        createClubData = new CreateClubData();
        createClubData.setClubName("club9");

        this.mockMvc.perform(post("/club/create")
                        .with(csrf())
                        .flashAttr("createClubData", createClubData))
                .andExpect(status().isOk())
                .andExpect(view().name("clubCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "failure"))
                .andExpect(model().attributeDoesNotExist("club"));
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testGetCreateTeams() throws Exception {
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club1");
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club3");
        when(applicationParameterService.getApplicationParameter("applicationStage"))
                .thenReturn(Optional.of("teamCreation"), Optional.of("notTeamCreation"));

        this.mockMvc.perform(get("/club/club1/createTeams"))
                .andExpect(status().isOk())
                .andExpect(view().name("createTeams"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("club", "club1"))
                .andExpect(model().attributeExists("createTeamsData"));

        this.mockMvc.perform(get("/club/club3/createTeams"))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongApplicationStage"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attributeDoesNotExist("club", "createTeamsData"));

        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
        verify(clubAdminService).validateClubAdminHasClubAccess("club3");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testPostCreateTeams () throws Exception {
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club1");
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club2");
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club3");
        when(applicationParameterService.getApplicationParameter("applicationStage"))
                .thenReturn(Optional.of("teamCreation"), Optional.of("teamCreation"), Optional.of("notTeamCreation"));

        CreateTeamsData createTeamsData = new CreateTeamsData();
        createTeamsData.setNumberOfTeamsToCreate(3);

        this.mockMvc.perform(post("/club/club1/createTeams")
                        .with(csrf())
                        .flashAttr("createTeamsData", createTeamsData))
                .andExpect(status().isOk())
                .andExpect(view().name("teamsCreated"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "success"));
        verify(teamCrudService).createTeams(createTeamsData);
        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
        assertEquals("club1",createTeamsData.getClubName());

        createTeamsData = new CreateTeamsData();
        createTeamsData.setNumberOfTeamsToCreate(2);
        doThrow(new BmmException("")).when(teamCrudService).createTeams(createTeamsData);
        this.mockMvc.perform(post("/club/club2/createTeams")
                        .with(csrf())
                        .flashAttr("createTeamsData", createTeamsData))
                .andExpect(status().isOk())
                .andExpect(view().name("teamsCreated"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "failure"));
        verify(clubAdminService).validateClubAdminHasClubAccess("club2");

        createTeamsData = new CreateTeamsData();
        createTeamsData.setNumberOfTeamsToCreate(4);
        this.mockMvc.perform(post("/club/club3/createTeams")
                        .with(csrf())
                        .flashAttr("createTeamsData", createTeamsData))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongApplicationStage"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))));
        verify(clubAdminService).validateClubAdminHasClubAccess("club3");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testGetRemoveTeams() throws Exception {
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club1");
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club3");
        when(applicationParameterService.getApplicationParameter("applicationStage"))
                .thenReturn(Optional.of("teamCreation"), Optional.of("notTeamCreation"));

        this.mockMvc.perform(get("/club/club1/removeTeams"))
                .andExpect(status().isOk())
                .andExpect(view().name("removeTeams"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("club", "club1"))
                .andExpect(model().attributeExists("removeTeamsData"));

        this.mockMvc.perform(get("/club/club3/removeTeams"))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongApplicationStage"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attributeDoesNotExist("club", "removeTeamsData"));

        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
        verify(clubAdminService).validateClubAdminHasClubAccess("club3");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testPostRemoveTeams () throws Exception {
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club1");
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club2");
        doNothing().when(clubAdminService).validateClubAdminHasClubAccess("club3");
        when(applicationParameterService.getApplicationParameter("applicationStage"))
                .thenReturn(Optional.of("teamCreation"), Optional.of("teamCreation"), Optional.of("notTeamCreation"));

        RemoveTeamsData removeTeamsData = new RemoveTeamsData();
        removeTeamsData.setNumberOfTeamsToDelete(3);

        this.mockMvc.perform(post("/club/club1/removeTeams")
                        .with(csrf())
                        .flashAttr("removeTeamsData", removeTeamsData))
                .andExpect(status().isOk())
                .andExpect(view().name("teamsRemoved"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "success"));
        verify(teamCrudService).removeTeams(removeTeamsData);
        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
        assertEquals("club1",removeTeamsData.getClubName());

        removeTeamsData = new RemoveTeamsData();
        removeTeamsData.setNumberOfTeamsToDelete(2);
        doThrow(new BmmException("")).when(teamCrudService).removeTeams(removeTeamsData);
        this.mockMvc.perform(post("/club/club2/removeTeams")
                        .with(csrf())
                        .flashAttr("removeTeamsData", removeTeamsData))
                .andExpect(status().isOk())
                .andExpect(view().name("teamsRemoved"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "failure"));
        verify(clubAdminService).validateClubAdminHasClubAccess("club2");

        removeTeamsData = new RemoveTeamsData();
        removeTeamsData.setNumberOfTeamsToDelete(4);
        this.mockMvc.perform(post("/club/club3/removeTeams")
                        .with(csrf())
                        .flashAttr("removeTeamsData", removeTeamsData))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongApplicationStage"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))));
        verify(clubAdminService).validateClubAdminHasClubAccess("club3");
    }
}