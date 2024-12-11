package net.ohnonick2.naturzooprojekt.db.permission;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String permission;

    private String description;

    public Permission() {}

    public Permission(String permission , String description) {
        this.permission = permission;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


    public String getDescription() {
        return description;
    }

}
