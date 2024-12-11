package net.ohnonick2.naturzooprojekt.db.permission;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;

@Entity(name = "rolleuser")
@IdClass(RolleUserId.class)
public class RolleUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "rolle_id", nullable = false)
    private Rolle rolle;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Pfleger user;

    public RolleUser() {}

    public RolleUser(Rolle rolle, Pfleger user) {
        this.rolle = rolle;
        this.user = user;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

    public Pfleger getUser() {
        return user;
    }

    public void setUser(Pfleger user) {
        this.user = user;
    }
}
