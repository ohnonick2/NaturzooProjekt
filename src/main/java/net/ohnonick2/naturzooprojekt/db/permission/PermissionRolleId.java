package net.ohnonick2.naturzooprojekt.db.permission;

import java.io.Serializable;
import java.util.Objects;

public class PermissionRolleId implements Serializable {

    private Long permission;
    private Long rolle;

    public PermissionRolleId() {}

    public PermissionRolleId(Long permission, Long rolle) {
        this.permission = permission;
        this.rolle = rolle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRolleId that = (PermissionRolleId) o;
        return Objects.equals(permission, that.permission) &&
                Objects.equals(rolle, that.rolle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission, rolle);
    }
}
