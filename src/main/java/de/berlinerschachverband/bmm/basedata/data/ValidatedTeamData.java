package de.berlinerschachverband.bmm.basedata.data;

public record ValidatedTeamData(TeamData teamData,
                                Boolean isValid) {
}
