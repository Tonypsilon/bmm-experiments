package de.berlinerschachverband.bmm.security.controller;

import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.RolesService;
import de.berlinerschachverband.bmm.security.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolesController.class)
@AutoConfigureTestDatabase
class RolesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private RolesService rolesService;

    @MockBean
    private UsersService usersService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testGetAddRole() throws Exception {
        when(usersService.getAllUserNames()).thenReturn(List.of("user1", "user2"));
        when(rolesService.getAllNonAdministratorRoleNaturalNames()).thenReturn(List.of("TeamAdmin","ClubAdmin","User"));

        this.mockMvc.perform(get("/roles/addUserRole"))
                .andExpect(status().isOk())
                .andExpect(view().name("addUserRole"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("usernames", List.of("user1", "user2")))
                .andExpect(model().attribute("roles", List.of("TeamAdmin", "ClubAdmin", "User")));
    }

}