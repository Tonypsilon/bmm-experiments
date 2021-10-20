package de.berlinerschachverband.bmm.exceptions;

public class UserAlreadyExistsException extends BmmException {
    public UserAlreadyExistsException(String username) {
        super("User %s already exists!".formatted(username));
    }
}
