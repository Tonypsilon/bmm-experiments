package de.berlinerschachverband.bmm.basedata.data;

import javax.persistence.*;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "season_id",
            foreignKey = @ForeignKey(name = "CLUB_SEASON_ID_FK"),
            nullable = false)
    private Season season;
}
