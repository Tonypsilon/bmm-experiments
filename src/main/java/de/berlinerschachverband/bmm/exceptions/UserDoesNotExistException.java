package de.berlinerschachverband.bmm.exceptions;

public class UserDoesNotExistException extends BmmException {

    public UserDoesNotExistException(String username) {
        super("User does not exist: %s".formatted(username));
    }
}
