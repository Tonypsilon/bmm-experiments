package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.TeamData;
import de.berlinerschachverband.bmm.basedata.data.ValidatedTeamData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.PrepareEditTeamData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.basedata.service.EditTeamService;
import de.berlinerschachverband.bmm.basedata.service.TeamValidationService;
import de.berlinerschachverband.bmm.config.service.ApplicationParameterService;
import de.berlinerschachverband.bmm.exceptions.BmmException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
@AutoConfigureTestDatabase
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private ClubService clubService;

    @MockBean
    private TeamValidationService teamValidationService;

    @MockBean
    private EditTeamService editTeamService;

    @MockBean
    private ClubAdminService clubAdminService;

    @MockBean
    private ApplicationParameterService applicationParameterService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }


    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testGetTeamsOfClub() throws Exception {
        Club club1 = new Club();
        club1.setName("club1");
        ClubData clubData1 = new ClubData(1L, "club1", true, 1);
        when(clubService.getClub("club1")).thenReturn(club1);
        when(clubService.toClubData(club1)).thenReturn(clubData1);
        when(teamValidationService.getTeamsOfClubValidated("club1")).thenReturn(List.of(
                new ValidatedTeamData(new TeamData(1L, clubData1, Optional.empty(), 1, 8), true),
                new ValidatedTeamData(new TeamData(2L, clubData1, Optional.empty(), 2, 6), false)
        ));

        this.mockMvc.perform(get("/club/club1/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("teamsOfClub"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("club", clubData1))
                .andExpect(model().attribute("teams", List.of(
                        new ValidatedTeamData(new TeamData(1L, clubData1, Optional.empty(), 1, 8), true),
                        new ValidatedTeamData(new TeamData(2L, clubData1, Optional.empty(), 2, 6), false)
                )));
        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testGetEditTeamOfClubSuccess() throws Exception {
        PrepareEditTeamData prepareEditTeamData = new PrepareEditTeamData();
        prepareEditTeamData.setClubName("club1");
        prepareEditTeamData.setAvailablePlayers(List.of());
        prepareEditTeamData.setTeamNumber(1);
        prepareEditTeamData.setMaxNumberOfPlayers(16);
        when(applicationParameterService.getApplicationParameter("applicationStage")).thenReturn(Optional.of("teamCreation"));
        when(editTeamService.getTeamForEditing("club1", 1)).thenReturn(prepareEditTeamData);

        this.mockMvc.perform(get("/club/club1/teams/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("editTeam"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("prepareEditTeamData", prepareEditTeamData));
        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testGetEditTeamsOfClubFailure() throws Exception {
        when(applicationParameterService.getApplicationParameter("applicationStage")).thenReturn(Optional.of("notTeamCreation"));

        this.mockMvc.perform(get("/club/club1/teams/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongApplicationStage"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))));
        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testPostEditTeamsOfClubSuccess() throws Exception {
        PrepareEditTeamData prepareEditTeamData = new PrepareEditTeamData();
        prepareEditTeamData.setClubName("club1");
        prepareEditTeamData.setAvailablePlayers(List.of());
        prepareEditTeamData.setTeamNumber(1);
        prepareEditTeamData.setMaxNumberOfPlayers(16);
        when(applicationParameterService.getApplicationParameter("applicationStage")).thenReturn(Optional.of("teamCreation"));

        this.mockMvc.perform(post("/club/club1/teams/1/edit")
                        .with(csrf())
                        .flashAttr("prepareEditTeamData", prepareEditTeamData))
                .andExpect(status().isOk())
                .andExpect(view().name("editedTeam"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "success"));
        verify(clubAdminService).validateClubAdminHasClubAccess("club1");
    }

    @Test
    @WithMockUser(authorities = Roles.CLUB_ADMIN)
    void testPostEditTeamsOfClubFailure() throws Exception {
        PrepareEditTeamData prepareEditTeamData = new PrepareEditTeamData();
        prepareEditTeamData.setClubName("club1");
        prepareEditTeamData.setAvailablePlayers(List.of());
        prepareEditTeamData.setTeamNumber(1);
        prepareEditTeamData.setMaxNumberOfPlayers(16);
        when(applicationParameterService.getApplicationParameter("applicationStage")).thenReturn(Optional.of("notTeamCreation"));

        this.mockMvc.perform(post("/club/club1/teams/1/edit")
                        .with(csrf())
                        .flashAttr("prepareEditTeamData", prepareEditTeamData))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongApplicationStage"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))));

        when(applicationParameterService.getApplicationParameter("applicationStage")).thenReturn(Optional.of("teamCreation"));
        doThrow(new BmmException("")).when(editTeamService).editTeam(prepareEditTeamData);

        this.mockMvc.perform(post("/club/club1/teams/1/edit")
                        .with(csrf())
                        .flashAttr("prepareEditTeamData", prepareEditTeamData))
                .andExpect(status().isOk())
                .andExpect(view().name("editedTeam"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("state", "failure"));
        verify(clubAdminService, times(2)).validateClubAdminHasClubAccess("club1");
    }
}