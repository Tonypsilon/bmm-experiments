package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    List<Division> findBySeason_Name(String seasonName);

    Optional<Division> findByNameAndSeason_Name(String divisionName, String seasonName);
}
