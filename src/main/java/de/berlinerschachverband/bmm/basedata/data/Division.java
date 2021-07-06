package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = false)
    private String name;

    @NonNull
    @Column(unique = false)
    private Integer level;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "season_id", foreignKey = @ForeignKey(name = "SEASON_ID_FK"))
    private Season season;

    public Division() {
    }

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

    @NonNull
    public Integer getLevel() {
        return level;
    }

    public void setLevel(@NonNull Integer level) {
        this.level = level;
    }

    @NonNull
    public Season getSeason() {
        return season;
    }

    public void setSeason(@NonNull Season season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Division division = (Division) o;
        return id.equals(division.id) && name.equals(division.name) && level.equals(division.level) && season.equals(division.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, season);
    }
}
