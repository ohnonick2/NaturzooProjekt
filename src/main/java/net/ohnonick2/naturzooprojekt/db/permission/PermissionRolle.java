package net.ohnonick2.naturzooprojekt.db.permission;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "permissionrolle")
@IdClass(PermissionRolleId.class)
public class PermissionRolle {

    @Id
    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @Id
    @ManyToOne
    @JoinColumn(name = "rolle_id", nullable = false)
    private Rolle rolle;



    public PermissionRolle() {}

    public PermissionRolle(Permission permission, Rolle rolle) {
        this.permission = permission;
        this.rolle = rolle;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

}
