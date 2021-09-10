package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    public Optional<Club> findByName(String name);

    public List<Club> findByActiveTrue();

}
