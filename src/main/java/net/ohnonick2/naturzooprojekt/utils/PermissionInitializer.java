package net.ohnonick2.naturzooprojekt.utils;


import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.service.PermissionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionService permissionService;

    public PermissionInitializer(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public void run(String... args) {

    }
}
