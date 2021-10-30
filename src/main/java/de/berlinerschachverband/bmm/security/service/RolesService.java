package de.berlinerschachverband.bmm.security.service;

import de.berlinerschachverband.bmm.exceptions.InvalidRoleException;
import de.berlinerschachverband.bmm.security.Roles;
import de.berlinerschachverband.bmm.security.data.Authorities;
import de.berlinerschachverband.bmm.security.data.AuthoritiesRepository;
import de.berlinerschachverband.bmm.security.data.Users;
import de.berlinerschachverband.bmm.security.data.thymeleaf.AddRoleData;
import de.berlinerschachverband.bmm.security.data.thymeleaf.RemoveRoleData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    private final UsersService usersService;
    private final AuthoritiesRepository authoritiesRepository;

    public RolesService(UsersService usersService, AuthoritiesRepository authoritiesRepository) {
        this.usersService = usersService;
        this.authoritiesRepository = authoritiesRepository;
    }

    /**
     * Assign a given role to a given user. Role must not be the ROLE_administrator.
     *
     * @param addRoleData
     */
    public void addUserRole(AddRoleData addRoleData) {
        String role = Roles.toNaturalName.inverse().get(addRoleData.getRoleNaturalName());
        if(Boolean.FALSE.equals(Roles.isValidNonAdministratorRole(role))) {
            throw new InvalidRoleException(addRoleData.getRoleNaturalName());
        }
        Users user = usersService.getUser(addRoleData.getUsername());
        if(authoritiesRepository.findByAuthorityAndUsersUsername(role, addRoleData.getUsername()).isPresent()) {
            return;
        }
        Authorities authorities = new Authorities();
        authorities.setAuthority(role);
        authorities.setUsers(user);

        authoritiesRepository.saveAndFlush(authorities);
    }

    /**
     * Remove a given user-role assignment. Role must not be the ROLE_administrator.
     * @param removeRoleData
     */
    public void removeUserRole(RemoveRoleData removeRoleData) {
        String role = Roles.toNaturalName.inverse().get(removeRoleData.getRoleNaturalName());
        if(Boolean.FALSE.equals(Roles.isValidNonAdministratorRole(role))) {
            throw new InvalidRoleException(role);
        }
        authoritiesRepository.findByAuthorityAndUsersUsername(role, removeRoleData.getUsername())
                .ifPresent(authoritiesRepository::delete);
    }

    public List<String> getAllNonAdministratorRoleNaturalNames() {
        return Roles.toNaturalName.keySet().stream()
                .filter(Roles::isValidNonAdministratorRole)
                .map(Roles.toNaturalName::get)
                .sorted()
                .toList();
    }

}
