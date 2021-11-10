package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

public class CreateDivisionData {

    private String name;
    private Integer level;
    private Integer numberOfBoards;
    private String seasonName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getNumberOfBoards() {
        return numberOfBoards;
    }

    public void setNumberOfBoards(Integer numberOfBoards) {
        this.numberOfBoards = numberOfBoards;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

}
