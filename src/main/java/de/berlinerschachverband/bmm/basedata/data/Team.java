package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id",
            foreignKey = @ForeignKey(name = "TEAM_CLUB_ID_FK"),
            nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "division_id",
    foreignKey = @ForeignKey(name = "TEAM_DIVISION_ID_FK"),
    nullable = true)
    private Division division;

    @Column(unique = false, nullable = false)
    private Integer number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
