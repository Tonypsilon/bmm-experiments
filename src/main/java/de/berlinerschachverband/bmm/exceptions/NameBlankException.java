package de.berlinerschachverband.bmm.exceptions;

public class NameBlankException extends BmmException{
    public NameBlankException(String message) {
        super(message);
    }

    public NameBlankException() {
        super("Name must not be blank!");
    }
}
