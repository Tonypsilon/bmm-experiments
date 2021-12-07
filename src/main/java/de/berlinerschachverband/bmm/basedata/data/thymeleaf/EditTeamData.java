package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

import java.util.List;

public class EditTeamData {

    private List<AddPlayerData> availablePlayers;

    private List<AddPlayerData> teamPlayers;

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

    public List<AddPlayerData> getTeamPlayers() {
        return teamPlayers;
    }

    public void setTeamPlayers(List<AddPlayerData> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public void addTeamPlayer(AddPlayerData addPlayerData) {
        teamPlayers.add(addPlayerData);
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
