package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

import java.util.List;

public class EditTeamData {

    private List<AddPlayerData> availablePlayers;

    private List<AddPlayerData> teamPlayers;

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
}
