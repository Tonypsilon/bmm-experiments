package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class AvailablePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "zps",
            foreignKey = @ForeignKey(name = "AVAILABLE_PLAYER_ZPS_FK"),
            nullable = false)
    private Club club;

    @Column(nullable = false)
    private Integer memberNumber;

    @Column(nullable = false)
    private Character active;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer birthYear;

    @Column
    private Integer dwz;

    @Column
    private Integer elo;

    @Column
    private String title;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
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
    public Integer getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(@NonNull Integer memberNumber) {
        this.memberNumber = memberNumber;
    }

    @NonNull
    public Character getActive() {
        return active;
    }

    public void setActive(@NonNull Character active) {
        this.active = active;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(@NonNull Integer birthYear) {
        this.birthYear = birthYear;
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
}
