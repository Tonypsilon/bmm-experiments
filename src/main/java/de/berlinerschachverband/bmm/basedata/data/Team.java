package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id",
            foreignKey = @ForeignKey(name = "TEAM_SEASON_ID_FK"),
            nullable = false)
    private Season season;

    @ManyToOne
    @JoinColumn(name = "club_id",
            foreignKey = @ForeignKey(name = "TEAM_CLUB_ID_FK"),
            nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "division_id",
    foreignKey = @ForeignKey(name = "TEAM_DIVISION_ID_FK"),
    nullable = false)
    private Division division;

    @Column(unique = false, nullable = false)
    private Integer number;

    @OneToOne
    private Team nextHigherTeam;

    @OneToOne
    private Team nextLowerTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public Season getSeason() {
        return season;
    }

    public void setSeason(@NonNull Season season) {
        this.season = season;
    }

    @NonNull
    public Club getClub() {
        return club;
    }

    public void setClub(@NonNull Club club) {
        this.club = club;
    }

    @NonNull
    public Division getDivision() {
        return division;
    }

    public void setDivision(@NonNull Division division) {
        this.division = division;
    }

    @NonNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(@NonNull Integer number) {
        this.number = number;
    }

    public Team getNextHigherTeam() {
        return nextHigherTeam;
    }

    public void setNextHigherTeam(Team nextHigherTeam) {
        this.nextHigherTeam = nextHigherTeam;
    }

    public Team getNextLowerTeam() {
        return nextLowerTeam;
    }

    public void setNextLowerTeam(Team nextLowerTeam) {
        this.nextLowerTeam = nextLowerTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id) && season.equals(team.season) && club.equals(team.club) && number.equals(team.number) && nextHigherTeam.equals(team.nextHigherTeam) && nextLowerTeam.equals(team.nextLowerTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, season, club, number, nextHigherTeam, nextLowerTeam);
    }
}
