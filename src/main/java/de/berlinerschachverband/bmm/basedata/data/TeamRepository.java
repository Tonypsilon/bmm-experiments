package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Set<Team> findByDivision_Id(Long divisionId);

    Optional<Team> findByClub_NameAndNumberAndDivisionIsNull(String clubName, Integer number);

    Set<Team> findByClub_NameAndDivisionIsNull(String clubName);
}
