package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

import java.util.List;

public class PrepareEditTeamData {

    private List<AddPlayerData> availablePlayers;

    private List<PlayerThymeleafData> currentTeamPlayers;

    private List<Integer> futureTeamPlayersMemberNumbers;

    private String clubName;

    private Integer teamNumber;

    private Integer numberOfBoards;

    private Integer maxNumberOfPlayers;

    private Boolean isLastTeam;

    public void setAvailablePlayers(List<AddPlayerData> availablePlayers) {
        this.availablePlayers = availablePlayers;
    }
    public List<AddPlayerData> getAvailablePlayers() {
        return availablePlayers;
    }

    public void setCurrentTeamPlayers(List<PlayerThymeleafData> currentTeamPlayers) {
        this.currentTeamPlayers = currentTeamPlayers;
    }
    public List<PlayerThymeleafData> getCurrentTeamPlayers() {
        return currentTeamPlayers;
    }

    public void setFutureTeamPlayersMemberNumbers(List<Integer> futureTeamPlayersMemberNumbers) {
        this.futureTeamPlayersMemberNumbers = futureTeamPlayersMemberNumbers;
    }

    public List<Integer> getFutureTeamPlayersMemberNumbers() {
        return futureTeamPlayersMemberNumbers;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setNumberOfBoards(Integer numberOfBoards) {
        this.numberOfBoards = numberOfBoards;
    }

    public Integer getNumberOfBoards() {
        return numberOfBoards;
    }

    public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public Integer getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public void setLastTeam(Boolean lastTeam) {
        isLastTeam = lastTeam;
    }

    public Boolean getLastTeam() {
        return isLastTeam;
    }
}
