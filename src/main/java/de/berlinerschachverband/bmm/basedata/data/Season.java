package de.berlinerschachverband.bmm.basedata.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = false, nullable = false)
    private Boolean archived;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(@NonNull Boolean archived) {
        this.archived = archived;
    }
}
