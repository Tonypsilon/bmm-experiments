package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String name;

    public Season() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return id.equals(season.id) && name.equals(season.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
