package net.ohnonick2.naturzooprojekt.backend.permission;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.repository.PermissionRepository;
import net.ohnonick2.naturzooprojekt.repository.PermissionRolleRepository;
import net.ohnonick2.naturzooprojekt.repository.RolleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/permissions")
public class PermissionManager {

    @Autowired
    private PermissionRolleRepository permissionRolleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolleRepository rolleRepository;

    @PostMapping("/removePermission")
    public ResponseEntity<String> removePermission(@RequestBody String body) {
        System.out.println(body);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("roleid") || !json.has("permissionid")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Rolle rolle = rolleRepository.findRolleById(json.get("roleid").getAsLong());
        Permission permission = permissionRepository.findPermissionById(json.get("permissionid").getAsLong());

        if (permission.getPermission().endsWith("_WRITE")) {
            Permission readPermission = permissionRepository.findPermissionByPermission(permission.getPermission().replace("_WRITE", "_READ"));

            PermissionRolle readPermissionRolle = permissionRolleRepository.findByRolleAndPermission(rolle, readPermission);
            if (readPermissionRolle != null) {
                permissionRolleRepository.delete(readPermissionRolle);
            } else {
                return ResponseEntity.badRequest().body("No read permission found");
            }


        }


        PermissionRolle permissionRolle = permissionRolleRepository.findByRolleAndPermission(rolle, permission);
        if (permissionRolle != null) {
            permissionRolleRepository.delete(permissionRolle);
        } else {
            return ResponseEntity.badRequest().body("No permission found");
        }

        return ResponseEntity.ok().body("Permission removed");
    }

    @PostMapping("/addPermission")
    public ResponseEntity<String> addPermission(@RequestBody String body) {
        System.out.println(body);

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("roleid") || !json.has("permissionid")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Rolle rolle = rolleRepository.findRolleById(json.get("roleid").getAsLong());
        Permission permission = permissionRepository.findPermissionById(json.get("permissionid").getAsLong());

        PermissionRolle permissionRolle = permissionRolleRepository.findByRolleAndPermission(rolle, permission);
        if (permissionRolle != null) {
            return ResponseEntity.badRequest().body("Permission already exists");
        } else {
            permissionRolle = new PermissionRolle(permission, rolle);
            permissionRolleRepository.save(permissionRolle);
        }

        // Wenn es sich um eine WRITE-Berechtigung handelt, füge die zugehörige READ-Berechtigung hinzu
        if (permission.getPermission().endsWith("_WRITE")) {
            Permission readPermission = permissionRepository.findPermissionByPermission(permission.getPermission().replace("_WRITE", "_READ"));

            // Überprüfe, ob die READ-Berechtigung existiert, und füge sie hinzu, wenn sie noch nicht existiert
            if (readPermission != null) {
                PermissionRolle readPermissionRolle = permissionRolleRepository.findByRolleAndPermission(rolle, readPermission);
                if (readPermissionRolle == null) {
                    permissionRolleRepository.save(new PermissionRolle(readPermission, rolle));
                }
            }
        }

        return ResponseEntity.ok().body("Permission added");
    }



}
