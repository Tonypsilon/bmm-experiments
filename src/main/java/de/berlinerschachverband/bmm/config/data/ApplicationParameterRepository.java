package de.berlinerschachverband.bmm.config.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationParameterRepository extends JpaRepository<ApplicationParameter, Long> {

    Optional<ApplicationParameter> findByKey(String key);
}
