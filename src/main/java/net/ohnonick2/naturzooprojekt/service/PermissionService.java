package net.ohnonick2.naturzooprojekt.service;

import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void savePermissions(List<Permission> permissions) {
        for (Permission permission : permissions) {
            if (!permissionRepository.existsById(permission.getId())) {
                permissionRepository.save(permission);
            }
        }
    }
}