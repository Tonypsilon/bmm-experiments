package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByTeam_IdAndBoardNumber(Long teamId, Integer number);

    Collection<Player> findByTeam_Id(Long teamId);
}
