package net.ohnonick2.naturzooprojekt.backend.pfleger;

import com.google.gson.JsonArray;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


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

        // Überprüfen, ob der Benutzer authentifiziert ist
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return ResponseEntity.badRequest().body("Benutzer ist nicht authentifiziert.");
        }

        // Benutzernamen des angemeldeten Benutzers abrufen
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        JsonObject userloggedIn;
        JsonObject targetUser;
        try {
            userloggedIn = JsonParser.parseString(username).getAsJsonObject();
            targetUser = JsonParser.parseString(body).getAsJsonObject();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Parsen der JSON-Daten.");
        }

        // Verhindern, dass der Benutzer sich selbst bearbeitet
        if (userloggedIn.get("id").getAsLong() == targetUser.get("id").getAsLong()) {
            return ResponseEntity.badRequest().body("Benutzer kann sich nicht selbst bearbeiten.");
        }

        // Ziel-Pfleger aus der Datenbank abrufen
        Pfleger pfleger = pflegerrepository.findById(targetUser.get("id").getAsLong()).orElse(null);
        if (pfleger == null) {
            return ResponseEntity.badRequest().body("Pfleger mit der angegebenen ID wurde nicht gefunden.");
        }

        try {
            // Aktualisierung der Pfleger-Daten
            pfleger.setBenutzername(targetUser.get("benutzername").getAsString());
            pfleger.setVorname(targetUser.get("vorname").getAsString());
            pfleger.setNachname(targetUser.get("nachname").getAsString());
            pfleger.setEnabled(targetUser.get("enabled").getAsBoolean());

            // Geburtsdatum setzen
            String geburtstag = targetUser.get("geburtstag").getAsString();
            if (geburtstag != null && !geburtstag.isEmpty()) {
                pfleger.setGeburtsdatum(LocalDate.parse(geburtstag)); // Erwartet ISO-Format (yyyy-MM-dd)
            } else {
                pfleger.setGeburtsdatum(null); // Geburtsdatum auf null setzen, falls leer
            }

            // Ort setzen
            JsonObject ortJson = targetUser.getAsJsonObject("ort");
            if (ortJson != null && ortJson.has("plz")) {
                int plz = ortJson.get("plz").getAsInt(); // PLZ extrahieren
                Ort ort = ortrepository.findByPlz(plz); // Ort aus der Datenbank abrufen
                if (ort != null) {
                    pfleger.setOrt(ort);
                } else {
                    return ResponseEntity.badRequest().body("Ort mit der angegebenen PLZ wurde nicht gefunden.");
                }
            } else {
                return ResponseEntity.badRequest().body("Ungültige oder fehlende Ort-Daten.");
            }

            // Änderungen speichern
            pflegerrepository.save(pfleger);
            return ResponseEntity.ok("Pfleger erfolgreich aktualisiert.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler bei der Aktualisierung des Pflegers.");
        }
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

    @PostMapping("/add")
    public ResponseEntity<String> addPfleger(@RequestBody String body) {
        System.out.println("ADD");

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return ResponseEntity.badRequest().body("Nicht authentifiziert");
        }

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();


        //check if user already exists
        Pfleger pflegerCheck = pflegerrepository.findByBenutzername(targetUser.get("benutzername").getAsString());
        if (pflegerCheck != null) {
            return ResponseEntity.badRequest().body("Benutzername bereits vergeben");
        }


        Pfleger pfleger = new Pfleger();
        pfleger.setBenutzername(targetUser.get("benutzername").getAsString());
        pfleger.setVorname(targetUser.get("vorname").getAsString());
        pfleger.setNachname(targetUser.get("nachname").getAsString());
        pfleger.setEnabled(targetUser.get("enabled").getAsBoolean());
        pfleger.setPassword(passwordEncoder.encode(targetUser.get("password").getAsString()));
        pfleger.setGeburtsdatum(LocalDate.parse(targetUser.get("geburtstag").getAsString()));
        System.out.println("rollen: " + targetUser.get("rollen").getAsString());

        Rolle rolle = rolleRepository.findRolleByName(targetUser.get("rollen").getAsString());
        if (rolle == null) {
            return ResponseEntity.badRequest().body("Rolle nicht gefunden");
        }






        // Ort prüfen
        if (!targetUser.has("ort")) {
            return ResponseEntity.badRequest().body("Ort fehlt im Request");
        }

        Ort ort = ortrepository.findByPlz(targetUser.get("ort").getAsInt());
        if (ort == null) {
            return ResponseEntity.badRequest().body("Ort nicht gefunden");
        }

        pfleger.setOrt(ort);
        pflegerrepository.save(pfleger);
        RolleUser rolleUser = new RolleUser();
        rolleUser.setUser(pfleger);
        rolleUser.setRolle(rolle);
        rolleUserRepository.save(rolleUser);

        return ResponseEntity.ok("Pfleger erfolgreich hinzugefügt");
    }


}