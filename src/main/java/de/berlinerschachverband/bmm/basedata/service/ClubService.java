package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.Club;
import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.ClubRepository;
import de.berlinerschachverband.bmm.exceptions.ClubNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    /**
     * Get all clubs, active and inactive, ordered alphabetically by their name.
     * @return
     */
    public List<ClubData> getAllClubs() {
        return clubRepository.findAll().stream()
                .sorted(Comparator.comparing(Club::getName))
                .map(this::toClubData)
                .toList();
    }

    /**
     * Get all clubs with status active, ordered alphabetically by their name.
     * @return
     */
    public List<ClubData> getAllActiveClubs() {
        return  clubRepository.findByActiveTrue().stream()
                .sorted(Comparator.comparing(Club::getName))
                .map(this::toClubData)
                .toList();
    }

    /**
     * Get a club, given its name. If not found, a ClubNotFoundException is thrown.
     * @param name
     * @return
     */
    public Club getClub(String name) {
        return clubRepository.findByName(name)
                .orElseThrow(() -> new ClubNotFoundException(name));
    }

    /**
     * Activate a club, given its name. If not found, a ClubNotFoundException is thrown.
     * @param clubName
     * @return
     */
    public void activateClub(String clubName) {
        Club clubToActivate = getClub(clubName);
        clubToActivate.setActive(true);
        clubRepository.saveAndFlush(clubToActivate);
    }

    /**
     * Deactivate a club, given its name. If not found, a ClubNotFoundException is thrown.
     * @param clubName
     * @return
     */
    public void deactivateClub(String clubName) {
        Club clubToDeactivate = getClub(clubName);
        clubToDeactivate.setActive(false);
        clubRepository.saveAndFlush(clubToDeactivate);
    }

    public ClubData toClubData(Club club) {
        return new ClubData(club.getId(), club.getName(), club.getActive());
    }
}
