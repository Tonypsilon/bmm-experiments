package de.berlinerschachverband.bmm.security.service;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.security.data.ClubAdmin;
import de.berlinerschachverband.bmm.security.data.ClubAdminRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
                .toList();
    }

    /**
     * Call this from within a request to check if a user, given by name, is
     * assigned to a club in the clubAdmin table.
     * Does nothing if access is okay, throws an AccessDeniedException else.
     * @param clubName
     */
    public void validateClubAdminHasClubAccess(String clubName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!findClubsByUsername(username)
                .stream()
                .map(ClubData::name)
                .toList()
                .contains(clubName)) {
            throw new AccessDeniedException("User %s is no clubAdmin for club %s".formatted(username, clubName));
        }
    }
}
