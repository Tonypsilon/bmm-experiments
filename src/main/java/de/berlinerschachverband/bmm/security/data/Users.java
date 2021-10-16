package de.berlinerschachverband.bmm.security.data;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Users {

    @Id
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = false, nullable = false)
    private String password;

    @Column(unique = false, nullable = false)
    private Boolean enabled;

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
