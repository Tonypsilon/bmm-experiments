package de.berlinerschachverband.bmm.exceptions;

public class SeasonNameBlankException extends BmmException{
    public SeasonNameBlankException(String message) {
        super(message);
    }

    public SeasonNameBlankException() {
        super("Season name must not be blank!");
    }
}
