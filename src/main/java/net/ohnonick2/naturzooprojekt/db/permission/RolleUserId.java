package net.ohnonick2.naturzooprojekt.db.permission;

import java.io.Serializable;
import java.util.Objects;

public class RolleUserId implements Serializable {

    private Long rolle;
    private Long user;

    public RolleUserId() {}

    public RolleUserId(Long rolle, Long user) {
        this.rolle = rolle;
        this.user = user;
    }

    public Long getRolle() {
        return rolle;
    }

    public void setRolle(Long rolle) {
        this.rolle = rolle;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolleUserId that = (RolleUserId) o;
        return Objects.equals(rolle, that.rolle) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rolle, user);
    }
}
