package de.berlinerschachverband.bmm.resultdata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class AvailablePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer zps;

    @Column(nullable = false)
    private Integer memberNumber;

    @Column(nullable = false)
    private Character active;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String surname;

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

    @NonNull
    public Character getActive() {
        return active;
    }

    public void setActive(@NonNull Character active) {
        this.active = active;
    }

    @NonNull
    public java.lang.String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull java.lang.String name) {
        this.fullName = name;
    }

    @NonNull
    public java.lang.String getSurname() {
        return surname;
    }

    public void setSurname(@NonNull java.lang.String surname) {
        this.surname = surname;
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

    public Optional<java.lang.String> getTitle() {
        return Optional.ofNullable(title);
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }
}
