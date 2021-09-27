package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

public class CreateTeamsData {

    private String clubName;
    private Integer numberOfTeams;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }
}
