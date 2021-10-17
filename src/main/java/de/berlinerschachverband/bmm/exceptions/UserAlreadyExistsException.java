package de.berlinerschachverband.bmm.exceptions;

public class UserAlreadyExistsException extends BmmException {
    public UserAlreadyExistsException(String username) {
        super(String.format("User %s already exists!", username));
    }
}
