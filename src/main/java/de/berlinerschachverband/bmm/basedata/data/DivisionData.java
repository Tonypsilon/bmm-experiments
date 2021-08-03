package de.berlinerschachverband.bmm.basedata.data;

public record DivisionData(Long id,
                           String divisionName,
                           Integer level,
                           SeasonData season) {
}
