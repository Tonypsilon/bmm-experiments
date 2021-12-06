package de.berlinerschachverband.bmm.basedata.data;

import de.berlinerschachverband.bmm.basedata.data.Team;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private String fullName;

    @Column(unique = false, nullable = false)
    private String surname;

    @Column(nullable = true)
    private String fideId;

    @ManyToOne
    @JoinColumn(name = "team_id",
            foreignKey = @ForeignKey(name = "PLAYER_TEAM_ID_FK"),
            nullable = false)
    private Team team;

    @Column(unique = false, nullable = false)
    private Integer boardNumber;

    @Column(unique = false, nullable = true)
    private Integer dwz;

    @Column(unique = false, nullable = true)
    private Integer elo;

    @Column(unique = false, nullable = true)
    private String title;

    @Column(unique = false, nullable = false)
    private Integer zps;

    @Column(unique = false, nullable = false)
    private Integer memberNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String name) {
        this.fullName = name;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    public Optional<String> getFideId() {
        return Optional.ofNullable(fideId);
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
    public Integer getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(@NonNull Integer number) {
        this.boardNumber = number;
    }

    public Optional<Integer> getDwz() {
        return Optional.ofNullable(dwz);
    }

    public void setDwz(Integer dwz) {
        this.dwz = dwz;
    }

    public Optional<Integer> getElo() {
        return Optional.ofNullable(elo);
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    public Integer getZps() {
        return zps;
    }

    public void setZps(@NonNull Integer zps) {
        this.zps = zps;
    }

    @NonNull
    public Integer getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(@NonNull Integer memberNumber) {
        this.memberNumber = memberNumber;
    }
}
