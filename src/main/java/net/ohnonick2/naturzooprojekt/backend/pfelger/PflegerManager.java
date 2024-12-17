package net.ohnonick2.naturzooprojekt.backend.pfelger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/pfleger")
public class PflegerManager {

    @Autowired
    private Pflegerrepository pflegerRepository;

    @Autowired
    private Ortrepository ortrepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PflegerManager() {
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addPfleger(@RequestBody String json) {
        System.out.printf("Received JSON: %s\n", json);

        // Parse the input JSON string
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        // Check if the Pfleger already exists
        if (pflegerRepository.findByBenutzername(jsonObject.get("benutzername").getAsString()) != null) {
            return ResponseEntity.badRequest().body("Pfleger already exists");
        }

        // Find the Ort based on PLZ
        Ort ort1 = ortrepository.findByPlz(Integer.valueOf(jsonObject.get("plz").getAsString()));

        if (ort1 == null) {
            return ResponseEntity.badRequest().body("Ort not found");
        }

        // Encrypt the password before saving
        String encryptedPassword = passwordEncoder.encode(jsonObject.get("passwort").getAsString());

        // Create a new Pfleger object with the encrypted password
        Pfleger pfleger = new Pfleger(
                jsonObject.get("benutzername").getAsString(),
                encryptedPassword,
                jsonObject.get("vorname").getAsString(),
                ort1
        );

        // Save the Pfleger
        pflegerRepository.save(pfleger);

        return ResponseEntity.ok("Pfleger added");
    }
}
