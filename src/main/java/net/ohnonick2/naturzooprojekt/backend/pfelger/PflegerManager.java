package net.ohnonick2.naturzooprojekt.backend.pfelger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.repository.RolleRepository;
import net.ohnonick2.naturzooprojekt.repository.RolleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/pfleger")
public class PflegerManager {

    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private RolleUserRepository rolleUserRepository;

    @Autowired
    private Ortrepository ortrepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PflegerManager() {
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deletePfleger(@RequestBody String body) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) return ResponseEntity.badRequest().build();

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();
        if (userloggedIn.get("id").getAsLong() == targetUser.get("id").getAsLong()) {
            return ResponseEntity.badRequest().build();
        }

        Pfleger pfleger = pflegerrepository.findById( targetUser.get("id").getAsLong()).get();
        if (pfleger == null) {
            return ResponseEntity.badRequest().build();
        }
        List<RolleUser> rolleUserList = rolleUserRepository.findAll();

        for (RolleUser rolleUser : rolleUserList) {
            if (pfleger.getId() == rolleUser.getUser().getId()) {
                rolleUserRepository.delete(rolleUser);
            }

        }


        pflegerrepository.delete(pfleger);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editPfleger(@RequestBody String body) {
        System.out.println("EDIT");
        if (SecurityContextHolder.getContext().getAuthentication() == null) return ResponseEntity.badRequest().build();

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();
        if (userloggedIn.get("id").getAsLong() == targetUser.get("id").getAsLong()) {
            return ResponseEntity.badRequest().build();
        }

        Pfleger pfleger = pflegerrepository.findById( targetUser.get("id").getAsLong()).get();
        if (pfleger == null) {
            return ResponseEntity.badRequest().build();
        }

        pfleger.setBenutzername(targetUser.get("benutzername").getAsString());
        pfleger.setVorname(targetUser.get("vorname").getAsString());
        pfleger.setNachname(targetUser.get("nachname").getAsString());
        pfleger.setEnabled(targetUser.get("enabled").getAsBoolean());

        Ort ort = ortrepository.findByPlz(targetUser.get("ort").getAsInt());
        pfleger.setOrt(ort);

        pflegerrepository.save(pfleger);
        return ResponseEntity.ok().build();



    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody String body) {
        System.out.println("TEST CHANGE");
        if (SecurityContextHolder.getContext().getAuthentication() == null) return ResponseEntity.badRequest().build();

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        System.out.println(username);
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDdd");
        System.out.println(body);

        JsonObject userLoggedIN = JsonParser.parseString(username).getAsJsonObject();
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();
        if (targetUser == null || userLoggedIN == null) return ResponseEntity.badRequest().body("Irgendwas ist schiefgelaufen");
        if (targetUser == userLoggedIN) return ResponseEntity.badRequest().body("Geht nicht ,weil geht nicht");

        Pfleger pfleger = pflegerrepository.findPflegerById(targetUser.get("id").getAsLong());
        pfleger.setPassword(passwordEncoder.encode(targetUser.get("password").getAsString()));

        pflegerrepository.save(pfleger);

        return ResponseEntity.ok().build();
    }

}