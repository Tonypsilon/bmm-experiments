package de.berlinerschachverband.bmm.resultdata.data;

import de.berlinerschachverband.bmm.basedata.data.TeamData;

import java.util.Optional;

public record PlayerData(Long id,
                         String fullName,
                         String surname,
                         Optional<String> fideId,
                         TeamData team,
                         Integer boardNumber,
                         Optional<Integer> dwz,
                         Optional<Integer> elo,
                         Optional<String> title,
                         Integer zps,
                         Integer memberNumber) {
}
