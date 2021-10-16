package de.berlinerschachverband.bmm.security.service;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.security.data.ClubAdmin;
import de.berlinerschachverband.bmm.security.data.ClubAdminRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubAdminService {

    private final ClubAdminRepository clubAdminRepository;
    private final ClubService clubService;

    public ClubAdminService(ClubAdminRepository clubAdminRepository,
                            ClubService clubService) {
        this.clubAdminRepository = clubAdminRepository;
        this.clubService = clubService;
    }

    /**
     * Provide a list of clubs (ordered alphabetically by club name) of all
     * the clubs that a user, given by username, has admin privileges for.
     * @param username
     * @return
     */
    public List<ClubData> findClubsByUsername(String username) {
        return clubAdminRepository.findByUsers_Username(username)
                .stream()
                .map(ClubAdmin::getClub)
                .map(clubService::toClubData)
                .sorted(Comparator.comparing(ClubData::name))
                .collect(Collectors.toList());
    }
}
