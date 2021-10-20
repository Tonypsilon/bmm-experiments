package de.berlinerschachverband.bmm.exceptions;

public class WrongPasswordException extends BmmException{

    public WrongPasswordException(String message) {
        super(message);
    }
}
