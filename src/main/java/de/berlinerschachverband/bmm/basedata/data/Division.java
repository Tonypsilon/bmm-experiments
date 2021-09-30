package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private String name;

    @Column(unique = false, nullable = false)
    private Integer level;

    @ManyToOne
    @JoinColumn(name = "season_id",
            foreignKey = @ForeignKey(name = "DIVISION_SEASON_ID_FK"),
            nullable = false)
    private Season season;

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

}
