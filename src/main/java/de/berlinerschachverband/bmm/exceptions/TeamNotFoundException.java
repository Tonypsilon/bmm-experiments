package de.berlinerschachverband.bmm.exceptions;

public class TeamNotFoundException extends BmmException {

    public TeamNotFoundException(Long teamId) {
        super("Team not found with id %s".formatted(teamId.toString()));
    }
}
