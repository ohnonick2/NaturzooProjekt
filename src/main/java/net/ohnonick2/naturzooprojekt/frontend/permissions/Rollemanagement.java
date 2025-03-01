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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        // 1) Aktuellen Benutzer ermitteln
        String userJson = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject jsonObject = JsonParser.parseString(userJson).getAsJsonObject();
        Long userId = jsonObject.get("id").getAsLong();

        // 2) Pfleger (aktueller User)
        Pfleger pfleger = pflegerRepository.findPflegerById(userId);
        if (pfleger == null) {
            // Falls nicht eingeloggt oder ungültig
            return "redirect:/login";
        }

        // 3) Alle Rollen abrufen und sortieren
        List<Rolle> rolleList = rolleRepository.findAll();
        // Falls du Superadmin nicht anzeigen willst, könnte man sie hier entfernen.
        // rolleList.removeIf(r -> "Superadmin".equalsIgnoreCase(r.getName()));
        rolleList.sort(Comparator.comparing(Rolle::getWeight));
        model.addAttribute("rollen", rolleList);

        // 4) Liste mit erweiterten Infos (Permissions, User) erstellen
        List<RolleInfo> rollenInfos = new ArrayList<>();
        for (Rolle r : rolleList) {
            RolleInfo info = new RolleInfo();
            info.setRolle(r);

            // Zugewiesene Permissions (ohne "*" herausfiltern)
            List<PermissionRolle> prList = permissionRolleRepository.findByRolle(r);
            prList.removeIf(pr -> "*".equalsIgnoreCase(pr.getPermission().getPermission()));
            info.setPermissionRolleList(prList);

            // Verfügbare Permissions = alle minus schon zugewiesene minus "*"
            List<Permission> allPerms = new ArrayList<>(permissionRepository.findAll());
            Permission star = permissionRepository.findPermissionByPermission("*");
            if (star != null) {
                allPerms.remove(star);
            }
            // bereits zugewiesene entfernen
            allPerms.removeIf(perm -> prList.stream()
                    .anyMatch(pr -> pr.getPermission().getId().equals(perm.getId())));
            info.setAvailablePermissions(allPerms);

            // Zugewiesene Pfleger
            List<RolleUser> ruList = rolleUserRepository.findByRolleId(r.getId());
            info.setRolleUserList(ruList);

            // Verfügbare Pfleger = alle minus die bereits eine Rolle haben
            List<Pfleger> allPfleger = new ArrayList<>(pflegerRepository.findAll());
            allPfleger.removeIf(p -> rolleUserRepository.existsByUserId(p.getId()));
            info.setAvailableUsers(allPfleger);

            rollenInfos.add(info);
        }
        model.addAttribute("rollenInfos", rollenInfos);

        // 5) Aktueller Benutzer => hat er WRITE-Permission?
        RolleUser rolleUser = rolleUserRepository.findByUserId(userId);
        if (rolleUser != null) {
            int currentUserWeight = rolleUser.getRolle().getWeight();
            model.addAttribute("currentUserWeight", currentUserWeight);

            List<PermissionRolle> userPermissionList = permissionRolleRepository.findByRolle(rolleUser.getRolle());
            boolean hasWritePermission = userPermissionList.stream()
                    .map(pr -> pr.getPermission().getPermission().toUpperCase())
                    .anyMatch(perm -> perm.equals("*")
                            || perm.equals("ROLE_MANAGEMENT_WRITE"));
            model.addAttribute("hasWritePermission", hasWritePermission);
        } else {
            model.addAttribute("currentUserWeight", 0);
            model.addAttribute("hasWritePermission", false);
        }

        // Weiter zum Thymeleaf-Template
        return "autharea/rollen/rollenmanagement";
    }

    // Hilfsklasse: Kapselt alle Infos rund um eine Rolle (Permissions, Users etc.)
    public static class RolleInfo {
        private Rolle rolle;
        private List<PermissionRolle> permissionRolleList;
        private List<Permission> availablePermissions;
        private List<RolleUser> rolleUserList;
        private List<Pfleger> availableUsers;

        public Rolle getRolle() {
            return rolle;
        }
        public void setRolle(Rolle rolle) {
            this.rolle = rolle;
        }
        public List<PermissionRolle> getPermissionRolleList() {
            return permissionRolleList;
        }
        public void setPermissionRolleList(List<PermissionRolle> permissionRolleList) {
            this.permissionRolleList = permissionRolleList;
        }
        public List<Permission> getAvailablePermissions() {
            return availablePermissions;
        }
        public void setAvailablePermissions(List<Permission> availablePermissions) {
            this.availablePermissions = availablePermissions;
        }
        public List<RolleUser> getRolleUserList() {
            return rolleUserList;
        }
        public void setRolleUserList(List<RolleUser> rolleUserList) {
            this.rolleUserList = rolleUserList;
        }
        public List<Pfleger> getAvailableUsers() {
            return availableUsers;
        }
        public void setAvailableUsers(List<Pfleger> availableUsers) {
            this.availableUsers = availableUsers;
        }
    }
}
