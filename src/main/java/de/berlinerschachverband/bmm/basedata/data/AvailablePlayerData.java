package de.berlinerschachverband.bmm.basedata.data;

import java.util.Optional;

public record AvailablePlayerData(Long id,
                                  Integer zps,
                                  Integer memberNumber,
                                  Character active,
                                  String fullName,
                                  String surname,
                                  Integer birthYear,
                                  Optional<Integer> dwz,
                                  Optional<Integer> elo,
                                  Optional<String> title) {
}
