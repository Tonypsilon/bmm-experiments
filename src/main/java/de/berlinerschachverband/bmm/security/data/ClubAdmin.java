package de.berlinerschachverband.bmm.security.data;

import de.berlinerschachverband.bmm.basedata.data.Club;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class ClubAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_username",
            foreignKey = @ForeignKey(name = "CLUBADMIN_USERNAME_FK"),
            nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "club_id",
            foreignKey = @ForeignKey(name = "CLUBADMIN_CLUB_ID_FK"),
            nullable = false)
    private Club club;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public Users getUser() {
        return users;
    }

    public void setUser(@NonNull Users users) {
        this.users = users;
    }

    @NonNull
    public Club getClub() {
        return club;
    }

    public void setClub(@NonNull Club club) {
        this.club = club;
    }
}
