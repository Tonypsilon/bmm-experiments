package de.berlinerschachverband.bmm.resultdata.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AvailablePlayerRepository extends JpaRepository<AvailablePlayer, Long> {

    Collection<AvailablePlayer> findByZps(Integer zps);

    Optional<AvailablePlayer> findByZpsAndMemberNumber(Integer zps, Integer memberNumber);
}
