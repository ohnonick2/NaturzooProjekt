package net.ohnonick2.naturzooprojekt.backend.pfleger;

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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    // ✅ Pfleger löschen
    @PostMapping("/delete")
    public ResponseEntity<String> deletePfleger(@RequestBody String body) {
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();
        Long pflegerId = targetUser.get("id").getAsLong();

        Pfleger pfleger = pflegerrepository.findById(pflegerId).orElse(null);
        if (pfleger == null) return ResponseEntity.badRequest().body("Pfleger nicht gefunden");

        List<RolleUser> rolleUserList = rolleUserRepository.findByRolleId(pfleger.getId());

        for (RolleUser rolleUser : rolleUserList) {
            rolleUserRepository.delete(rolleUser);
        }
        pflegerrepository.delete(pfleger);

        return ResponseEntity.ok("Pfleger erfolgreich gelöscht");
    }

    // ✅ Pfleger bearbeiten
    @PostMapping("/edit")
    public ResponseEntity<String> editPfleger(@RequestBody String body) {
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();

        System.out.println("PFLEGER BEARBEITEN: " + "\n" + body);

        Long pflegerId = targetUser.get("id").getAsLong();

        Pfleger pfleger = pflegerrepository.findById(pflegerId).orElse(null);
        if (pfleger == null) return ResponseEntity.badRequest().body("Pfleger nicht gefunden");

        pfleger.setBenutzername(targetUser.get("benutzername").getAsString());
        pfleger.setVorname(targetUser.get("vorname").getAsString());
        pfleger.setNachname(targetUser.get("nachname").getAsString());
        pfleger.setEnabled(targetUser.get("enabled").getAsBoolean());
        pfleger.setGeburtsdatum(LocalDate.parse(targetUser.get("geburtsdatum").getAsString()));

        try {
            if (targetUser.has("lockedUntil")) {
                String lockedUntilStr = targetUser.get("lockedUntil").getAsString();

                // Format anpassen an das JSON-Format: "yyyy-MM-dd'T'HH:mm"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                // String in LocalDateTime umwandeln
                LocalDateTime lockedUntil = LocalDateTime.parse(lockedUntilStr, formatter);
                pfleger.setLockedUntil(lockedUntil);
            }
        } catch (DateTimeParseException e) {
            System.err.println("Fehler beim Parsen von lockedUntil: " + e.getMessage());
        }


        Ort ort;
        try {
            int ortPlz = targetUser.get("ort").getAsInt();
            ort = ortrepository.findByPlz(ortPlz);
        } catch (NumberFormatException e) {
            String ortName = targetUser.get("ort").getAsString();
            ort = ortrepository.findByOrtname(ortName);
        }
        if (ort == null) return ResponseEntity.badRequest().body("Ort nicht gefunden");

        pflegerrepository.save(pfleger);
        return ResponseEntity.ok("Pfleger erfolgreich aktualisiert");
    }


    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody String body) {
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();
        Pfleger pfleger = pflegerrepository.findById(targetUser.get("id").getAsLong()).orElse(null);

        if (pfleger == null) return ResponseEntity.badRequest().body("Pfleger nicht gefunden");

        pfleger.setPassword(passwordEncoder.encode(targetUser.get("password").getAsString()));
        pflegerrepository.save(pfleger);

        return ResponseEntity.ok("Passwort erfolgreich geändert");
    }


    @PostMapping("/add")
    public ResponseEntity<String> addPfleger(@RequestBody String body) {
        JsonObject targetUser = JsonParser.parseString(body).getAsJsonObject();
        System.out.println("PFLEGER HINZUFÜGEN: " + "\n" + body);
        if (pflegerrepository.findByBenutzername(targetUser.get("benutzername").getAsString()) != null) {
            return ResponseEntity.badRequest().body("Benutzername bereits vergeben");
        }

        //if enebled is not set, set it to true
        if (!targetUser.has("enabled")) {
            targetUser.addProperty("enabled", true);
        }



        Pfleger pfleger = new Pfleger();
        pfleger.setBenutzername(targetUser.get("benutzername").getAsString());
        pfleger.setVorname(targetUser.get("vorname").getAsString());
        pfleger.setNachname(targetUser.get("nachname").getAsString());
        pfleger.setEnabled(targetUser.get("enabled").getAsBoolean());

        pfleger.setPassword(passwordEncoder.encode(targetUser.get("passwort").getAsString()));
        pfleger.setGeburtsdatum(LocalDate.parse(targetUser.get("geburtsdatum").getAsString()));


        Ort ort;
        try {
            int ortPlz = targetUser.get("ort").getAsInt();
            ort = ortrepository.findByPlz(ortPlz);

        } catch (NumberFormatException e) {
            String ortName = targetUser.get("ort").getAsString();
            ort = ortrepository.findByOrtname(ortName);
        }
        if (ort == null) return ResponseEntity.badRequest().body("Ort nicht gefunden");


        pfleger.setOrt(ort);
        pflegerrepository.save(pfleger);

        return ResponseEntity.ok("Pfleger erfolgreich hinzugefügt");
    }
}
