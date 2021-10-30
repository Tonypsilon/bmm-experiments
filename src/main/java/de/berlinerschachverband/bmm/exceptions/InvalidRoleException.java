package de.berlinerschachverband.bmm.exceptions;

public class InvalidRoleException extends BmmException {

    public InvalidRoleException(String roleName) {
        super("The role name %s is not valid".formatted(roleName));
    }
}
