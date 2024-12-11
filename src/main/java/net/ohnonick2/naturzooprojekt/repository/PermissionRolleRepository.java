package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRolleRepository extends JpaRepository<PermissionRolle, Long> {

    // Suche alle PermissionRolle-Einträge nach einer bestimmten Permission
    List<PermissionRolle> findByPermission(Permission permission);

    // Suche alle PermissionRolle-Einträge nach einer bestimmten Rolle
    List<PermissionRolle> findByRolle(Rolle rolle);
}