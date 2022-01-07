package de.berlinerschachverband.bmm.config.data;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class ApplicationParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String applicationParameterKey;

    @Column(unique = false, nullable = false)
    private String applicationParameterValue;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getApplicationParameterKey() {
        return applicationParameterKey;
    }

    public void setApplicationParameterKey(@NonNull String key) {
        this.applicationParameterKey = key;
    }

    @NonNull
    public String getApplicationParameterValue() {
        return applicationParameterValue;
    }

    public void setApplicationParameterValue(@NonNull String value) {
        this.applicationParameterValue = value;
    }
}
