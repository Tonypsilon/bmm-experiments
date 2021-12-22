package de.berlinerschachverband.bmm.resultdata.service;

import de.berlinerschachverband.bmm.basedata.data.Division;
import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.service.DivisionService;
import de.berlinerschachverband.bmm.basedata.service.TeamDataAccessService;
import de.berlinerschachverband.bmm.exceptions.MatchDayAlreadyExistsException;
import de.berlinerschachverband.bmm.resultdata.data.MatchDay;
import de.berlinerschachverband.bmm.resultdata.data.MatchDayRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Match days are containers for matches basically, keeping track of round boardNumber and division
 * that the matches belong to.
 */
@Service
public class MatchDayService {

    private final DivisionService divisionService;

    private final TeamDataAccessService teamDataAccessService;

    private final MatchDayRepository matchDayRepository;

    public MatchDayService(DivisionService divisionService,
                           MatchDayRepository matchDayRepository,
                           TeamDataAccessService teamDataAccessService) {
        this.divisionService = divisionService;
        this.matchDayRepository = matchDayRepository;
        this.teamDataAccessService = teamDataAccessService;
    }

    /**
     * Creates match days for a complete season.
     * @param seasonData
     */
    public void createRoundRobinMatchDaysForSeason(SeasonData seasonData) {
        Collection<DivisionData> divisions = divisionService.getDivisionsOfSeason(seasonData);
        createRoundRobinMatchDaysForDivisions(divisions);
    }

    /**
     * Calls createMatchDaysForDivision for each of the given divisions with numberOfTeams - 1 match days.
     * Does nothing for any division of the given divisions that has less than 2 teams assigned.
     * Ignores divisions that already have match days assigned, just logs a warning.
     *
     * @param divisions
     */
    public void createRoundRobinMatchDaysForDivisions(Collection<DivisionData> divisions) {
        for(DivisionData division : divisions) {
            try {
                createMatchDaysForDivision(division, numberOfMatchDays(teamDataAccessService.getNumberOfTeamsOfDivision(division)));
            } catch (MatchDayAlreadyExistsException ex) {
                // TODO: Log warning properly.
                System.out.println("WARNING: " + ex.getMessage());
                continue;
            }
        }
    }

    /**
     * Given a division and a boardNumber of match days, create the match day entities. Fails if the division does not
     * exist or already has match days assigned. Does nothing if numberOfMatchDays is less than 1.
     * @param divisionData
     * @param numberOfMatchDays
     */
    public void createMatchDaysForDivision(DivisionData divisionData, Integer numberOfMatchDays) {
        if( numberOfMatchDays <1) {
            return;
        }
        Division division = divisionService.getDivisionByNameAndSeasonName(divisionData.name(), divisionData.season().name());
        for(int matchDayNumber = 1; matchDayNumber <= numberOfMatchDays; matchDayNumber++) {
            if(matchDayRepository.findByDivision_IdAndAndMatchDayNumber(divisionData.id(),matchDayNumber).isPresent()) {
                throw new MatchDayAlreadyExistsException("division: " + divisionData.name() + ", matchDayNumber: " + matchDayNumber);
            }
            MatchDay matchDay = new MatchDay();
            matchDay.setMatchDayNumber(matchDayNumber);
            matchDay.setDivision(division);
            matchDayRepository.save(matchDay);
        }
    }

    private Integer numberOfMatchDays(Integer numberOfTeams) {
        if (numberOfTeams %2 == 0) {
            return numberOfTeams -1;
        }else {
            return numberOfTeams;
        }
    }
}
