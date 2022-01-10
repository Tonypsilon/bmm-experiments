package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

import java.util.List;

public class PrepareEditTeamData {

    private List<AddPlayerData> availablePlayers;

    private List<PlayerThymeleafData> teamPlayers;

    private String clubName;

    private Integer teamNumber;

    private Integer numberOfBoards;

    private Boolean isLastTeam;

    public List<AddPlayerData> getAvailablePlayers() {
        return availablePlayers;
    }

    public void setAvailablePlayers(List<AddPlayerData> availablePlayers) {
        this.availablePlayers = availablePlayers;
    }

    public List<PlayerThymeleafData> getTeamPlayers() {
        return teamPlayers;
    }

    public void setTeamPlayers(List<PlayerThymeleafData> teamPlayers) {
        this.teamPlayers = teamPlayers;
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

    public void setLastTeam(Boolean lastTeam) {
        isLastTeam = lastTeam;
    }

    public Boolean getLastTeam() {
        return isLastTeam;
    }
}
