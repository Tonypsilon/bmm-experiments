package de.berlinerschachverband.bmm.basedata.data;

import java.util.List;
import java.util.Optional;

public record TeamData(Long id,
                       Season season,
                       Club club,
                       Integer number,
                       Optional<TeamData> nextHigherTeam,
                       Optional<TeamData> nextLowerTeam,
                       List<PlayerData> players) {
}
