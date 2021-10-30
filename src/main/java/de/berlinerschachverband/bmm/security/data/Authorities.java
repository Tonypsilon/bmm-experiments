package de.berlinerschachverband.bmm.security.data;

import javax.persistence.*;

@Entity
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username",
    foreignKey = @ForeignKey(name = "FK_AUTHORITIES_USERS"),
    nullable = false)
    private Users users;

    @Column(unique = false, nullable = false)
    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
