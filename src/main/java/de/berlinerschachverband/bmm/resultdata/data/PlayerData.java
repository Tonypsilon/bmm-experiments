package de.berlinerschachverband.bmm.resultdata.data;

import de.berlinerschachverband.bmm.basedata.data.TeamData;

import java.util.Optional;

public record PlayerData(Long id,
                         String name,
                         Optional<String> fideId,
                         TeamData team,
                         Integer number) {
}
