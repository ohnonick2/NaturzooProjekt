package net.ohnonick2.naturzooprojekt.frontend.permissions;

import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

        List<Rolle> rollen = rolleRepository.findAll();
        rollen.removeIf(rolle -> rolle.getName().equalsIgnoreCase("Superadmin"));

        rollen.sort((rolle1, rolle2) -> {
            return Integer.compare(rolle1.getWeight(), rolle2.getWeight());
        });



        model.addAttribute("rollen", rollen);
        return "autharea/rollen/rollenmanagement";
    }

    @GetMapping("/editRolle/{id}")
    public String edotRolle(@PathVariable Long id, Model model) {
        Rolle rolle = rolleRepository.findRolleById(id);
        List<PermissionRolle> permissionRolleList = permissionRolleRepository.findByRolle(rolle);



        permissionRolleList.removeIf(permissionRolle -> {
            return permissionRolle.getPermission().getPermission().equalsIgnoreCase("*");
        });

        List<Permission> permissions = permissionRepository.findAll();

        Permission allPermission = permissionRepository.findPermissionByPermission("*");
        if (allPermission != null) {
            permissions.remove(allPermission);
        }

        permissions.removeIf(permission -> {
            for (PermissionRolle permissionRolle : permissionRolleList) {
                if (permission.getId().equals(permissionRolle.getPermission().getId())) {
                    return true;
                }
            }
            return false;
        });

        //gebe nur die User zurück die die Rolle haben
        List<RolleUser> rolleUserList = rolleUserRepository.findByRolleId(rolle.getId());
        List<Pfleger> availableUsers = pflegerRepository.findAll();

        availableUsers.removeIf(pfleger -> {
            for (RolleUser rolleUser : rolleUserList) {

                if (rolleUser.getRolle().name.equalsIgnoreCase("Superadmin")) {
                    return false;
                }

                if (pfleger.getId().equals(rolleUser.getUser().getId())) {
                    return true;
                }
            }
            return false;
        });



        model.addAttribute("rolle", rolle);
        model.addAttribute("permissionRolleList", permissionRolleList);
        model.addAttribute("permissions", permissions);
        model.addAttribute("rolleUserList", rolleUserList);
        model.addAttribute("availableUsers", availableUsers);
        return "autharea/rollen/editrollenmanagement";
    }






}
