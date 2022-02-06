package de.berlinerschachverband.bmm.exceptions;

public class AvailablePlayerNotFoundException extends BmmException {

    public AvailablePlayerNotFoundException(Integer zps, Integer memberNumber) {
        super("Available player does not exist, zps: %s, memberNumber: %s."
                .formatted(zps.toString(), memberNumber.toString()));
    }
}
