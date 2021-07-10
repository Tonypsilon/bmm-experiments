package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "season_id", foreignKey = @ForeignKey(name = "SEASON_ID_FK"))
    private Season season;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "club_id", foreignKey = @ForeignKey(name = "CLUB_ID_FK"))
    private Club club;

    @NonNull
    @Column(unique = false)
    private Integer number;

}
