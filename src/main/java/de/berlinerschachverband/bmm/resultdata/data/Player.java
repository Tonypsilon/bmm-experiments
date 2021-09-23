package de.berlinerschachverband.bmm.resultdata.data;

import de.berlinerschachverband.bmm.basedata.data.Team;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private String name;

    @Column(nullable = true)
    private String fideId;

    @ManyToOne
    @JoinColumn(name = "team_id",
            foreignKey = @ForeignKey(name = "PLAYER_TEAM_ID_FK"),
            nullable = false)
    private Team team;

    @Column(unique = false, nullable = false)
    private Integer number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getFideId() {
        return fideId;
    }

    public void setFideId(String fideId) {
        this.fideId = fideId;
    }

    @NonNull
    public Team getTeam() {
        return team;
    }

    public void setTeam(@NonNull Team team) {
        this.team = team;
    }

    @NonNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(@NonNull Integer number) {
        this.number = number;
    }
}
