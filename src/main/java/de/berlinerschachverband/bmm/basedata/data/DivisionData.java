package de.berlinerschachverband.bmm.basedata.data;

public record DivisionData(Long id,
                           String name,
                           Integer level,
                           SeasonData season) {
}
