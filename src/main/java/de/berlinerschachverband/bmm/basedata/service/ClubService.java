package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

    public ClubData toClubData(Club club) {
        return new ClubData(club.getId(),
                club.getName());
    }
}
