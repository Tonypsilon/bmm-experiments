package de.berlinerschachverband.bmm.security.data.thymeleaf;

public class RemoveRoleData {

    private String username;
    private String roleNaturalName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleNaturalName() {
        return roleNaturalName;
    }

    public void setRoleNaturalName(String roleNaturalName) {
        this.roleNaturalName = roleNaturalName;
    }
}
