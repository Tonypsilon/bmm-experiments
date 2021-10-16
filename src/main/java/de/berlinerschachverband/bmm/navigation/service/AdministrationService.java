package de.berlinerschachverband.bmm.navigation.service;

import de.berlinerschachverband.bmm.navigation.data.AdministrationButtonData;
import de.berlinerschachverband.bmm.security.Roles;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class AdministrationService {

    public List<AdministrationButtonData> getAdministrationButtonData(Collection<String> roles) {
        List<AdministrationButtonData> administrationButtons = new ArrayList();
        if(roles.contains(Roles.administrator)) {
            administrationButtons.addAll(getAdministratorButtons());
        }
        if(roles.contains(Roles.clubAdmin)) {
            administrationButtons.addAll(getClubAdminButtons());
        }
        if(roles.contains(Roles.teamAdmin)) {
            administrationButtons.addAll(getTeamAdminButtons());
        }
        return administrationButtons;
    }

    private List<AdministrationButtonData> getAdministratorButtons() {
        return List.of(
                new AdministrationButtonData("/administration/createSeason", "Neue Saison erstellen"),
                new AdministrationButtonData("/administration/createDivision", "Neue Staffel erstellen"),
                new AdministrationButtonData("/clubs", "Alle Vereine anzeigen"),
                new AdministrationButtonData("/clubs/create", "Neuen Verein erstellen")
        );
    }

    private List<AdministrationButtonData> getClubAdminButtons() {
        return Collections.emptyList();
    }

    private List<AdministrationButtonData> getTeamAdminButtons() {
        return Collections.emptyList();
    }

}
