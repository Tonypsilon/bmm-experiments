package de.berlinerschachverband.bmm.resultdata.service;

import de.berlinerschachverband.bmm.basedata.data.Division;
import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.basedata.data.SeasonData;
import de.berlinerschachverband.bmm.basedata.service.DivisionService;
import de.berlinerschachverband.bmm.basedata.service.TeamService;
import de.berlinerschachverband.bmm.exceptions.DivisionAlreadyHasMatchDaysException;
import de.berlinerschachverband.bmm.resultdata.data.MatchDay;
import de.berlinerschachverband.bmm.resultdata.data.MatchDayRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Match days are containers for matches basically, keeping track of round number and division
 * that the matches belong to.
 */
@Service
public class MatchDayService {

    private final DivisionService divisionService;

    private final TeamService teamService;

    private final MatchDayRepository matchDayRepository;

    public MatchDayService(DivisionService divisionService,
                           MatchDayRepository matchDayRepository,
                           TeamService teamService) {
        this.divisionService = divisionService;
        this.matchDayRepository = matchDayRepository;
        this.teamService = teamService;
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
                createMatchDaysForDivision(division, teamService.getNumberOfTeamsOfDivision(division)-1);
            } catch (DivisionAlreadyHasMatchDaysException ex) {
                // TODO: Log warning properly.
                System.out.println("WARNING: division " + division.name() + " already has match days - ignored this one.");
                continue;
            }
        }
    }

    /**
     * Given a division and a number of match days, create the match day entities. Fails if the division does not
     * exist or already has match days assigned. Does nothing if numberOfMatchDays is less than 1.
     * @param divisionData
     * @param numberOfMatchDays
     */
    public void createMatchDaysForDivision(DivisionData divisionData, Integer numberOfMatchDays) {
        if( numberOfMatchDays <1) {
            return;
        }
        if( !matchDayRepository.findByDivision_Id(divisionData.id()).isEmpty()) {
            throw new DivisionAlreadyHasMatchDaysException("");
        }
        Division division = divisionService.getDivisionByNameAndSeasonName(divisionData.name(), divisionData.season().name());
        for(int matchDayNumber = 1; matchDayNumber <= numberOfMatchDays; matchDayNumber++) {
            MatchDay matchDay = new MatchDay();
            matchDay.setMatchDayNumber(matchDayNumber);
            matchDay.setDivision(division);
            matchDayRepository.save(matchDay);
        }
    }
}
