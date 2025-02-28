package net.ohnonick2.naturzooprojekt.frontend.pfleger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.repository.RolleRepository;
import net.ohnonick2.naturzooprojekt.repository.RolleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class Pflegermanagement {

    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private RolleUserRepository rolleUserRepository;

    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private Ortrepository ortrepository;

    @GetMapping("/usermanagement")
    public String userManagement(Model model) {
        // Fetch all users (Pfleger) and their associated RolleUser records
        List<Pfleger> pflegerList = pflegerrepository.findAll();
        List<RolleUser> rolleUserList = rolleUserRepository.findAll();

        // Map Pfleger IDs to their associated roles
        Map<Long, List<Rolle>> userRolesMap = rolleUserList.stream()
                .collect(Collectors.groupingBy(
                        rolleUser -> rolleUser.getUser().getId(), // Group by Pfleger ID
                        Collectors.mapping(
                                RolleUser::getRolle, // Map directly to Rolle
                                Collectors.toList()
                        )
                ));

        // Prepare a view-friendly list of users with roles
        List<UserWithRoles> usersWithRoles = pflegerList.stream()
                .map(pfleger -> new UserWithRoles(
                        pfleger,
                        userRolesMap.getOrDefault(pfleger.getId(), List.of()) // Attach roles
                ))
                .collect(Collectors.toList());

        // Add the list to the model
        model.addAttribute("usersWithRoles", usersWithRoles);

        // Get the logged-in user's details using Spring Security
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject jsonObject = JsonParser.parseString(username).getAsJsonObject();
        // Assuming `findByBenutzername` fetches the user based on the username
        Pfleger loggedInUser = pflegerrepository.findByBenutzername(jsonObject.get("benutzername").getAsString());
        // Add the logged-in user's ID and roles to the model
        model.addAttribute("loggedInUserId", loggedInUser.getId());
        model.addAttribute("loggedInUserRoles", userRolesMap.getOrDefault(loggedInUser.getId(), List.of()));
        List<Ort> orte = ortrepository.findAll();
        model.addAttribute("orte", orte);
        model.addAttribute("pflegerListe" , pflegerList);
        // Return the Thymeleaf template path
        return "autharea/pflegemanagement/plegemanagement";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Pfleger pfleger = pflegerrepository.findById(id).orElse(null);
        if (pfleger == null) {
            return "redirect:/usermanagement";
        }

        List<Ort> orte = ortrepository.findAll(); // Alle Orte abrufen

        model.addAttribute("pfleger", pfleger);
        model.addAttribute("orte", orte); // Orte in das Model einfügen

        List<Rolle> roles = rolleRepository.findAll();
        Rolle superAdmin = rolleRepository.findRolleByName("SuperAdmin");
        roles.remove(superAdmin);
        model.addAttribute("roles", roles); // Alle Rollen abrufen

        return "autharea/pflegemanagement/editpflegermanagement";
    }

    @GetMapping("/addPfleger")
    public String addPfleger(Model model) {
        List<Ort> orte = ortrepository.findAll(); // Alle Orte abrufen

        model.addAttribute("orte", orte); // Orte in das Model einfügen

        List<Rolle> roles = rolleRepository.findAll();

        Rolle superAdmin = rolleRepository.findRolleByName("SuperAdmin");
        roles.remove(superAdmin);
        model.addAttribute("roles", roles); // Alle Rollen abrufen
        return "autharea/pflegemanagement/addpflegermanament";
    }

    public static class UserWithRoles {
        private final Pfleger pfleger;
        private final List<Rolle> roles;
        private final boolean isSuperadmin;
        private final boolean isAdmin;

        private String roleNames;

        public UserWithRoles(Pfleger pfleger, List<Rolle> roles) {
            this.pfleger = pfleger;
            this.roles = roles;
            this.isAdmin = roles.stream().anyMatch(role -> "Admin".equalsIgnoreCase(role.getName()));
            this.isSuperadmin = roles.stream().anyMatch(role -> "SuperAdmin".equalsIgnoreCase(role.getName()));

            this.roleNames = roles.stream().map(Rolle::getName).collect(Collectors.joining(", "));
        }

        public Pfleger getPfleger() {
            return pfleger;
        }

        public List<Rolle> getRoles() {
            return roles;
        }

        public boolean isSuperadmin() {
            return isSuperadmin;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public String getRoleNames() {
            return roleNames;
        }
    }


}