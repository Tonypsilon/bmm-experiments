package de.berlinerschachverband.bmm.resultdata.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByTeam_IdAndBoardNumber(Long teamId, Integer number);
}
