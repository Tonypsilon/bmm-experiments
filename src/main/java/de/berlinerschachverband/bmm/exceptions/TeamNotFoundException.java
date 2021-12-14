package de.berlinerschachverband.bmm.exceptions;

public class TeamNotFoundException extends BmmException {

    public TeamNotFoundException(Long teamId) {
        super("Team not found with id %s".formatted(teamId.toString()));
    }

    public TeamNotFoundException(String clubName, Integer teamNumber) {
        super("Team number %d not found for club %s".formatted(teamNumber,clubName));
    }
}
