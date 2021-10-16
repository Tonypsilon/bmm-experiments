package de.berlinerschachverband.bmm.security.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubAdminRepository extends JpaRepository<ClubAdmin, Long> {

    List<ClubAdmin> findByUsers_Username(String username);
}
