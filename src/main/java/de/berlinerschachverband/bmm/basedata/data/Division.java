package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private String name;

    @Column(unique = false, nullable = false)
    private Integer level;

    @Column(unique = false, nullable = false)
    private Integer numberOfBoards;

    @ManyToOne
    @JoinColumn(name = "season_id",
            foreignKey = @ForeignKey(name = "DIVISION_SEASON_ID_FK"),
            nullable = false)
    private Season season;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
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

    @NonNull
    public Integer getNumberOfBoards() {
        return this.numberOfBoards;
    }

    public void setNumberOfBoards(@NonNull Integer numberOfBoards) {
        this.numberOfBoards = numberOfBoards;
    }
}
