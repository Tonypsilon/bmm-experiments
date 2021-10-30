package de.berlinerschachverband.bmm.navigation.service;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.navigation.data.AdministrationButtonData;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdministrationServiceTest {

    private AdministrationService administrationService;

    private final ClubAdminService clubAdminService = mock(ClubAdminService.class);

    @BeforeEach
    private void setUp() {
        administrationService = new AdministrationService(clubAdminService);
        when(clubAdminService.findClubsByUsername("testuser")).thenReturn(List.of(new ClubData(1L, "club", true)));
    }

    @Test
    void testGetAdministrationButtonDataAllRoles() {
        assertEquals(List.of(new AdministrationButtonData("/administration/createSeason", "Neue Saison erstellen"),
                        new AdministrationButtonData("/administration/createDivision", "Neue Staffel erstellen"),
                        new AdministrationButtonData("/clubs", "Alle Vereine anzeigen"),
                        new AdministrationButtonData("/club/create", "Neuen Verein erstellen"),
                        new AdministrationButtonData("/administration/createUser", "Neuen Benutzer erstellen"),
                        new AdministrationButtonData("/administration/club/club","Verein club verwalten")),
                administrationService.getAdministrationButtonData("testuser", List.of(
                        Roles.CLUB_ADMIN, Roles.TEAM_ADMIN, Roles.ADMINISTRATOR)));
    }

    @Test
    void testGetAdministrationButtonDataAdministrator() {
        assertEquals(List.of(new AdministrationButtonData("/administration/createSeason", "Neue Saison erstellen"),
                        new AdministrationButtonData("/administration/createDivision", "Neue Staffel erstellen"),
                        new AdministrationButtonData("/clubs", "Alle Vereine anzeigen"),
                        new AdministrationButtonData("/club/create", "Neuen Verein erstellen"),
                        new AdministrationButtonData("/administration/createUser", "Neuen Benutzer erstellen")),
                administrationService.getAdministrationButtonData("testuser", List.of(Roles.ADMINISTRATOR)));
    }

    @Test
    void testGetAdministrationButtonDataClubAdmin() {
        assertEquals(List.of(new AdministrationButtonData("/administration/club/club","Verein club verwalten")),
                administrationService.getAdministrationButtonData("testuser", List.of(Roles.CLUB_ADMIN)));
    }

    @Test
    void testGetAdministrationButtonDataTeamAdmin() {
        assertEquals(Collections.emptyList(),
                administrationService.getAdministrationButtonData("testuser", List.of(Roles.TEAM_ADMIN)));
    }
}