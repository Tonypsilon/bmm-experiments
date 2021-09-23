package de.berlinerschachverband.bmm.resultdata.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchDayRepository extends JpaRepository<MatchDay, Long> {

    List<MatchDay> findByDivision_Id(Long divisionId);
}
