package de.berlinerschachverband.bmm.security.controller;

import de.berlinerschachverband.bmm.exceptions.UserAlreadyExistsException;
import de.berlinerschachverband.bmm.security.data.thymeleaf.CreateUserData;
import de.berlinerschachverband.bmm.navigation.data.NavbarData;
import de.berlinerschachverband.bmm.navigation.service.NavbarService;
import de.berlinerschachverband.bmm.security.Roles;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
@AutoConfigureTestDatabase
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private UsersService usersService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testGetCreateUserShouldReturnCreateUserPage() throws Exception {
        this.mockMvc.perform(get("/administration/createUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attributeExists("createUserData"));
    }

    @Test
    @WithMockUser(authorities = {Roles.USER, Roles.CLUB_ADMIN, Roles.TEAM_ADMIN})
    void testGetCreateUserAccessDenied() throws Exception {
        this.mockMvc.perform(get("/administration/createUser"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testPostCreateUserSuccess() throws Exception {
        CreateUserData createUserData = new CreateUserData();
        createUserData.setUsername("username");
        createUserData.setPassword("password");
        createUserData.setPasswordConfirm("password");

        this.mockMvc.perform(post("/administration/createUser")
                        .with(csrf())
                        .flashAttr("createUserData", createUserData))
                .andExpect(status().isOk())
                .andExpect((view().name("userCreated")))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("username", "username"))
                .andExpect(model().attribute("state", "success"));
        verify(usersService).createUser(refEq(createUserData));
    }

    @Test
    @WithMockUser(authorities = Roles.ADMINISTRATOR)
    void testPostCreateUserFailure() throws Exception {
        CreateUserData createUserDataEmptyPassword1 = new CreateUserData();
        createUserDataEmptyPassword1.setUsername("username");
        createUserDataEmptyPassword1.setPassword("");
        createUserDataEmptyPassword1.setPasswordConfirm("");

        CreateUserData createUserDataEmptyPassword2 = new CreateUserData();
        createUserDataEmptyPassword2.setUsername("username");
        createUserDataEmptyPassword2.setPassword("");
        createUserDataEmptyPassword2.setPasswordConfirm("abc");

        CreateUserData createUserDataPasswordMismatch = new CreateUserData();
        createUserDataPasswordMismatch.setUsername("username");
        createUserDataPasswordMismatch.setPassword("password");
        createUserDataPasswordMismatch.setPasswordConfirm("otherPassword");

        CreateUserData createUserAlreadyExists = new CreateUserData();
        createUserAlreadyExists.setUsername("user1");
        createUserAlreadyExists.setPassword("password");
        createUserAlreadyExists.setPasswordConfirm("password");
        doThrow(new UserAlreadyExistsException("user1")).when(usersService).createUser(refEq(createUserAlreadyExists));

        this.mockMvc.perform(post("/administration/createUser")
                        .with(csrf())
                        .flashAttr("createUserData", createUserDataEmptyPassword1))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attributeDoesNotExist("state"))
                .andExpect(model().attribute("errorMessage", "Das Passwort darf nicht leer sein."));

        this.mockMvc.perform(post("/administration/createUser")
                        .with(csrf())
                        .flashAttr("createUserData", createUserDataEmptyPassword2))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attributeDoesNotExist("state"))
                .andExpect(model().attribute("errorMessage", "Das Passwort darf nicht leer sein."));

        this.mockMvc.perform(post("/administration/createUser")
                        .with(csrf())
                        .flashAttr("createUserData", createUserDataPasswordMismatch))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attributeDoesNotExist("state"))
                .andExpect(model().attribute("errorMessage", "Die Passwörter stimmen nicht überein."));

        verify(usersService, never()).createUser(any());

        this.mockMvc.perform(post("/administration/createUser")
                        .with(csrf())
                        .flashAttr("createUserData", createUserAlreadyExists))
                .andExpect(status().isOk())
                .andExpect(view().name("userCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "failure"));
    }

    @Test
    @WithMockUser(authorities = {Roles.USER, Roles.CLUB_ADMIN, Roles.TEAM_ADMIN})
    void testPostCreateUserAccessDenied() throws Exception {
        this.mockMvc.perform(post("/administration/createUser"))
                .andExpect(status().isForbidden());
    }
}