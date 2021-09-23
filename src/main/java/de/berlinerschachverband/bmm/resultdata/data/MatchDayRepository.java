package de.berlinerschachverband.bmm.resultdata.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchDayRepository extends JpaRepository<MatchDay, Long> {

    List<MatchDay> findByDivision_Id(Long divisionId);

    Optional<MatchDay> findByDivision_IdAndAndMatchDayNumber(Long divisionId, Integer matchDayNumber);
}
