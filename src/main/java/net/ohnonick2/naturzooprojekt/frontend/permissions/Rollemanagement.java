package net.ohnonick2.naturzooprojekt.frontend.permissions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class Rollemanagement {

    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private RolleUserRepository rolleUserRepository;

    @Autowired
    private PermissionRolleRepository permissionRolleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private Pflegerrepository pflegerRepository;







    @GetMapping("/rollen")
    public String rolleManagement(Model model) {
        // Aktuellen Benutzer ermitteln
        String json = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Long userId = jsonObject.get("id").getAsLong();

        // Benutzer-Validierung
        Pfleger pfleger = pflegerRepository.findPflegerById(userId);
        if (pfleger == null) {
            return "redirect:/login";
        }

        // Rollen abrufen und sortieren
        List<Rolle> rolleList = rolleRepository.findAll();
        rolleList.removeIf(rolle -> "Superadmin".equalsIgnoreCase(rolle.getName())); // Entferne Superadmin

        rolleList.sort(Comparator.comparing(Rolle::getWeight));
        model.addAttribute("rollen", rolleList);

        // Aktuelle Benutzerrolle und Gewicht
        RolleUser rolleUser = rolleUserRepository.findByUserId(userId);
        model.addAttribute("currentUserWeight", rolleUser.getRolle().getWeight());
        model.addAttribute("currentUserId", userId);

        // Berechtigungen der aktuellen Rolle
        List<PermissionRolle> permissionRolleList = permissionRolleRepository.findByRolle(rolleUser.getRolle());
        model.addAttribute("hasWritePermission", permissionRolleList.stream().anyMatch(p -> "WRITE".equalsIgnoreCase(p.getPermission().getPermission())));




        return "autharea/rollen/rollenmanagement";
    }


    @GetMapping("/editRolle/{id}")
    public String editRolle(@PathVariable Long id, Model model) {
        Rolle rolle = rolleRepository.findRolleById(id);
        List<PermissionRolle> permissionRolleList = permissionRolleRepository.findByRolle(rolle);

        permissionRolleList.removeIf(permissionRolle ->
                permissionRolle.getPermission().getPermission().equalsIgnoreCase("*")
        );

        List<Permission> permissions = permissionRepository.findAll();
        Permission allPermission = permissionRepository.findPermissionByPermission("*");
        if (allPermission != null) permissions.remove(allPermission);

        permissions.removeIf(permission -> permissionRolleList.stream()
                .anyMatch(permissionRolle -> permission.getId().equals(permissionRolle.getPermission().getId()))
        );

        List<RolleUser> rolleUserList = rolleUserRepository.findByRolleId(rolle.getId());
        List<Pfleger> availableUsers = new ArrayList<>(pflegerRepository.findAll());

        // Entferne Pfleger, die bereits in einer Rolle sind
        availableUsers.removeIf(pfleger -> rolleUserRepository.existsByUserId(pfleger.getId()));

        model.addAttribute("rolle", rolle);
        model.addAttribute("permissionRolleList", permissionRolleList);
        model.addAttribute("permissions", permissions);
        model.addAttribute("rolleUserList", rolleUserList);
        model.addAttribute("availableUsers", availableUsers);

        return "autharea/rollen/editrollenmanagement";
    }


    @GetMapping("/addRolle")
    public String addRolle(Model model) {


        String json = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        Long userId = jsonObject.get("id").getAsLong();

        RolleUser rolleUser = rolleUserRepository.findByUserId(userId);


        model.addAttribute("currentUserWeight", rolleUser.getRolle().getWeight());


        return "autharea/rollen/addrollenmanagement";
    }







}
