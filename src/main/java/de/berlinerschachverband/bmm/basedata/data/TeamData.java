package de.berlinerschachverband.bmm.basedata.data;

import java.util.Optional;

public record TeamData(Long id,
                       SeasonData season,
                       ClubData clubData,
                       Integer number,
                       Optional<TeamData> nextHigherTeam,
                       Optional<TeamData> nextLowerTeam,
                       DivisionData division) {
}
