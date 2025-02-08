package net.ohnonick2.naturzooprojekt.backend.permission;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/permissions")
public class PermissionManager {

    @Autowired
    private PermissionRolleRepository permissionRolleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private RolleUserRepository rolleUserRepository;

    @Autowired
    private Pflegerrepository pflegerRepository;


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

    @PostMapping("/edit/rolle")
    public ResponseEntity<String> editRolle(@RequestBody String body) {
        System.out.println(body);

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("roleid") || !json.has("rolename")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Rolle rolle = rolleRepository.findRolleById(json.get("roleid").getAsLong());
        if (rolle == null) {
            return ResponseEntity.badRequest().body("Role not found");
        }

        rolle.setName(json.get("rolename").getAsString());
        rolleRepository.save(rolle);

        return ResponseEntity.ok().body("Role edited");
    }

    @PostMapping("/caretakers/add")
    public ResponseEntity<String> addPfleger(@RequestBody String body){

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (jsonObject == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!jsonObject.has("rolleid") || !jsonObject.has("userid")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Rolle rolle = rolleRepository.findRolleById(jsonObject.get("rolleid").getAsLong());

        Pfleger pfleger = pflegerRepository.findPflegerById(jsonObject.get("userid").getAsLong());

        if (pfleger == null) {
            return ResponseEntity.badRequest().body("Pfleger not found");
        }

        if (rolle == null) {
            return ResponseEntity.badRequest().body("Rolle not found");
        }

        RolleUser rolleUser = new RolleUser(rolle , pfleger);
        if (rolleUserRepository.findByUserId(pfleger.getId()) != null) {
            return ResponseEntity.badRequest().body("Pfleger already has a role");
        }

        rolleUserRepository.save(rolleUser);
        return ResponseEntity.ok().body("Pfleger added");
    }

    @PostMapping("/add/rolle")
    public ResponseEntity<String> addRolle(@RequestBody String body) {
        System.out.println(body);

        JsonObject json;
        try {
            json = JsonParser.parseString(body).getAsJsonObject();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON format.");
        }

        if (!json.has("rolename") || !json.has("weight")) {
            return ResponseEntity.badRequest().body("Missing required fields: 'rolename' and 'weight'.");
        }

        String roleName = json.get("rolename").getAsString();
        int roleWeight = json.get("weight").getAsInt();

        // **Aktuellen Benutzer ermitteln**
        String jsonUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userJson = JsonParser.parseString(jsonUser).getAsJsonObject();
        Long userId = userJson.get("id").getAsLong();

        RolleUser currentUserRole = rolleUserRepository.findByUserId(userId);
        if (currentUserRole == null) {
            return ResponseEntity.badRequest().body("Current user role not found.");
        }

        int currentUserWeight = currentUserRole.getRolle().getWeight();

        // **Gewicht validieren – Neue Rolle muss höher sein als aktuelle**
        if (roleWeight <= currentUserWeight) {
            return ResponseEntity.badRequest().body("Das Gewicht der neuen Rolle muss größer als " + currentUserWeight + " sein.");
        }

        // **Neue Rolle speichern**
        Rolle rolle = new Rolle(roleName, roleWeight);
        rolleRepository.save(rolle);

        return ResponseEntity.ok().body("Role added successfully.");
    }


    @PostMapping("/caretakers/remove")
    public ResponseEntity<String> removePfleger(@RequestBody String body){

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (jsonObject == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!jsonObject.has("rolleid") || !jsonObject.has("userid")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Rolle rolle = rolleRepository.findRolleById(jsonObject.get("rolleid").getAsLong());

        Pfleger pfleger = pflegerRepository.findPflegerById(jsonObject.get("userid").getAsLong());

        if (pfleger == null) {
            return ResponseEntity.badRequest().body("Pfleger not found");
        }

        if (rolle == null) {
            return ResponseEntity.badRequest().body("Rolle not found");
        }

        RolleUser rolleUser = rolleUserRepository.findByUserId(pfleger.getId());
        if (rolleUser == null) {
            return ResponseEntity.badRequest().body("Pfleger has no role");
        }

        rolleUserRepository.delete(rolleUser);
        return ResponseEntity.ok().body("Pfleger removed");
    }







}
