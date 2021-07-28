package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository {

    List<Team> findBySeason_Id(Long seasonId);
}
