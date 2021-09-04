package de.berlinerschachverband.bmm.basedata.data;

import java.util.Optional;

public record PlayerData(Long id,
                         String name,
                         Optional<String> fideId,
                         TeamData team,
                         Integer number) {
}
