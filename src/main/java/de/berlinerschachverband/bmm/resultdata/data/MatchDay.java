package de.berlinerschachverband.bmm.resultdata.data;

import de.berlinerschachverband.bmm.basedata.data.Division;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"match_day_number", "division_id"}))
public class MatchDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private Integer matchDayNumber;

    @ManyToOne
    @JoinColumn(name = "division_id",
            foreignKey = @ForeignKey(name = "MATCHDAY_DIVISION_ID_FK"),
            nullable = false)
    private Division division;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatchDayNumber() {
        return matchDayNumber;
    }

    public void setMatchDayNumber(Integer matchDayNumber) {
        this.matchDayNumber = matchDayNumber;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }
}
