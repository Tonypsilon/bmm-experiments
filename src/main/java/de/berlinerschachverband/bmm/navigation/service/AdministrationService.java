package de.berlinerschachverband.bmm.navigation.service;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.navigation.data.AdministrationButtonData;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.service.ClubAdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class AdministrationService {

    private final ClubAdminService clubAdminService;

    public AdministrationService(ClubAdminService clubAdminService) {
        this.clubAdminService = clubAdminService;
    }

    public List<AdministrationButtonData> getAdministrationButtonData(String username, Collection<String> roles) {
        List<AdministrationButtonData> administrationButtons = new ArrayList();
        if(roles.contains(Roles.ADMINISTRATOR)) {
            administrationButtons.addAll(getAdministratorButtons());
        }
        if(roles.contains(Roles.CLUB_ADMIN)) {
            administrationButtons.addAll(getClubAdminButtons(username));
        }
        if(roles.contains(Roles.TEAM_ADMIN)) {
            administrationButtons.addAll(getTeamAdminButtons(username));
        }
        return administrationButtons;
    }

    private List<AdministrationButtonData> getAdministratorButtons() {
        return List.of(
                new AdministrationButtonData("/administration/createSeason", "Neue Saison erstellen"),
                new AdministrationButtonData("/administration/createDivision", "Neue Staffel erstellen"),
                new AdministrationButtonData("/clubs", "Alle Vereine anzeigen"),
                new AdministrationButtonData("/clubs/create", "Neuen Verein erstellen"),
                new AdministrationButtonData("/administration/createUser", "Neuen Benutzer erstellen")
        );
    }

    private List<AdministrationButtonData> getClubAdminButtons(String username) {
        return clubAdminService.findClubsByUsername(username).stream()
                .map(this::constructClubAdminButtonFromClub)
                .toList();
    }

    private AdministrationButtonData constructClubAdminButtonFromClub(ClubData clubData) {
        return new AdministrationButtonData(
                String.format("/administration/club/%s", clubData.name()),
                String.format("Verein %s verwalten", clubData.name())
        );
    }

    private List<AdministrationButtonData> getTeamAdminButtons(String username) {
        return Collections.emptyList();
    }

}
