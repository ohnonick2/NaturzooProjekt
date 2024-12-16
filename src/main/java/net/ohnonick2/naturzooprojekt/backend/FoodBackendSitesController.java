package net.ohnonick2.naturzooprojekt.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import net.ohnonick2.naturzooprojekt.repository.PermissionRolleRepository;
import net.ohnonick2.naturzooprojekt.repository.RolleUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FoodBackendSitesController {

    @Autowired
    private RolleUserRepository rolleUserRepository;

    @Autowired
    private PermissionRolleRepository permissionRolleRepository;

    @Autowired
    private Ortrepository ortrepository;

    @PostMapping("/say/{value}")
    public ResponseEntity<String> sayHello(@PathVariable String value) {
        if (value == null || value.isEmpty()) {
            return ResponseEntity.badRequest().body("Value is null or empty");
        }

        try {
            Long userId = Long.valueOf(value);
            RolleUser user = rolleUserRepository.findByUserId(userId);

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Pfleger pfleger = user.getUser();
            Rolle rolle = user.getRolle();

            if (rolle == null) {
                return ResponseEntity.badRequest().body("Role not found");
            }

            List<PermissionRolle> permissionRolles = permissionRolleRepository.findByRolle(rolle);

            if (permissionRolles == null || permissionRolles.isEmpty()) {
                return ResponseEntity.badRequest().body("No permissions found");
            }

            StringBuilder permissions = new StringBuilder();
            for (PermissionRolle permissionRolle : permissionRolles) {
                Permission permission = permissionRolle.getPermission();
                if (permission != null) {
                    permissions.append(permission.getPermission())
                            .append(" (")
                            .append(permission.getDescription())
                            .append("), ");
                }
            }

            // Remove the last comma and space
            permissions.setLength(permissions.length() - 2);

            return ResponseEntity.ok(permissions.toString());

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid user ID format");
        }
    }
    
    @PostMapping("/get/user/{value}")
    public ResponseEntity<String> getUser(@PathVariable String value) {
        if (value == null || value.isEmpty()) {
          return ResponseEntity.badRequest().body("Value is null or empty");
        }

        try {
            Long userId = Long.valueOf(value);
            RolleUser rolleUser = rolleUserRepository.findByUserId(userId);

            if (rolleUser == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            Pfleger pfleger = rolleUser.getUser();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", pfleger.getId());
            jsonObject.addProperty("vorname", pfleger.getVorname());
            jsonObject.addProperty("nachname", pfleger.getNachname());

            // Add Ort and PLZ if available
            if (pfleger.getOrt() != null) {
                jsonObject.addProperty("plz", pfleger.getOrt().getPlz());
                jsonObject.addProperty("ort", pfleger.getOrt().getOrtname());
            } else {
                jsonObject.addProperty("plz", "N/A");
                jsonObject.addProperty("ort", "N/A");
            }

            Rolle rolle = rolleUser.getRolle();
            List<PermissionRolle> permissionRolles = permissionRolleRepository.findByRolle(rolle);

            JsonArray permissions = new JsonArray();
            if (permissionRolles != null && !permissionRolles.isEmpty()) {
                for (PermissionRolle permissionRolle : permissionRolles) {
                    Permission permission = permissionRolle.getPermission();
                    if (permission != null) {
                        JsonObject permissionObject = new JsonObject();
                        permissionObject.addProperty("permission", permission.getPermission());
                        permissionObject.addProperty("description", permission.getDescription());
                        permissions.add(permissionObject);
                    }
                }
                jsonObject.add("permissions", permissions);
            } else {
                jsonObject.addProperty("permissions", "No permissions found");
            }

            return ResponseEntity.ok(jsonObject.toString());

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid user ID format");
        }
    }

}
