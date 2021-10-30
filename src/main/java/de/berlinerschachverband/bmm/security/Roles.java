package de.berlinerschachverband.bmm.security;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

import java.util.List;
import java.util.Map;

public class Roles {

    public static final String ADMINISTRATOR = "ROLE_administrator";
    public static final String TEAM_ADMIN = "ROLE_teamAdmin";
    public static final String CLUB_ADMIN = "ROLE_clubAdmin";
    public static final String USER = "ROLE_user";

    private Roles() { }

    public static boolean isValidRoleName(String roleName) {
        return List.of(ADMINISTRATOR, TEAM_ADMIN, CLUB_ADMIN, USER)
                .contains(roleName);
    }

    public static boolean isValidNonAdministratorRole(String roleName) {
        return List.of(TEAM_ADMIN, CLUB_ADMIN, USER)
                .contains(roleName);
    }

    public static final ImmutableBiMap<String, String> toNaturalName =
            ImmutableBiMap.of(
                    ADMINISTRATOR, "Administrator",
                    TEAM_ADMIN, "TeamAdmin",
                    CLUB_ADMIN, "ClubAdmin",
                    USER, "User"
            );


}
