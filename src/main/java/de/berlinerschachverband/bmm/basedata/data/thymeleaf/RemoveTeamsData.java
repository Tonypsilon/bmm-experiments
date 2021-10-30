package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

public class RemoveTeamsData {

    private String clubName;
    private int numberOfTeamsToDelete;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getNumberOfTeamsToDelete() {
        return numberOfTeamsToDelete;
    }

    public void setNumberOfTeamsToDelete(int numberOfTeamsToDelete) {
        this.numberOfTeamsToDelete = numberOfTeamsToDelete;
    }

}
