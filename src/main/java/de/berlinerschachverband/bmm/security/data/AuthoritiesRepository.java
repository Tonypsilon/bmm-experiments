package de.berlinerschachverband.bmm.security.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

    public Optional<Authorities> findByAuthorityAndUsersUsername(String authority, String username);
}
