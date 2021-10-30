package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

public class CreateTeamsData {

    private String clubName;
    private Integer numberOfTeamsToCreate;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Integer getNumberOfTeamsToCreate() {
        return numberOfTeamsToCreate;
    }

    public void setNumberOfTeamsToCreate(Integer numberOfTeamsToCreate) {
        this.numberOfTeamsToCreate = numberOfTeamsToCreate;
    }
}
