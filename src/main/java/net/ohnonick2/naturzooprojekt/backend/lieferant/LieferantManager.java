package net.ohnonick2.naturzooprojekt.backend.lieferant;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.adresse.Adresse;
import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.repository.AdresseRepository;
import net.ohnonick2.naturzooprojekt.repository.FutterRepositority;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lieferant")
public class LieferantManager {

    @Autowired
    private LieferantRepository lieferantRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private Ortrepository ortrepository;

    @Autowired
    private FutterRepositority futterRepositority;

    @PostMapping("/add")
    public ResponseEntity<String> addLieferant(@RequestBody String body) {
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            if (json == null) {
                return ResponseEntity.badRequest().body("Invalid JSON");
            }

            // Validierung der Hauptattribute
            if (!json.has("name") || !json.has("telefon") || !json.has("email") || !json.has("adresseId")) {
                return ResponseEntity.badRequest().body("Fehlende Attribute: Name, Telefon, E-Mail oder AdresseID.");
            }

            long adresseId = json.get("adresseId").getAsLong();
            Adresse adresse = adresseRepository.findById(adresseId)
                    .orElse(null);

            if (adresse == null) {
                return ResponseEntity.badRequest().body("Adresse mit ID " + adresseId + " nicht gefunden.");
            }

            // Neues Lieferant-Objekt erstellen und speichern
            Lieferant lieferant = new Lieferant(
                    json.get("name").getAsString(),
                    adresse,
                    json.get("telefon").getAsString(),
                    json.get("email").getAsString()
            );
            lieferantRepository.save(lieferant);

            return ResponseEntity.ok("Lieferant erfolgreich hinzugefügt!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Verarbeiten der Anfrage: " + e.getMessage());
        }
    }


    @PostMapping("/delete")
    public ResponseEntity<String> deleteLieferant(@RequestBody String body) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("id")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }


        Lieferant lieferant = lieferantRepository.findLieferantById(json.get("id").getAsLong());
        if (lieferant == null) {
            return ResponseEntity.badRequest().body("Lieferant nicht vorhanden");
        }

        //wenn futter noch lieferant hat return badrequest
        if (futterRepositority.findFutterById(lieferant.getId()).getLieferant().equals(lieferant)) {
            return ResponseEntity.badRequest().body("Lieferant hat noch Futter");
        }

        lieferantRepository.delete(lieferant);
        return ResponseEntity.ok("Lieferant deleted");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editLieferant(@RequestBody String body) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("id") || !json.has("name") || !json.has("telefonnummer") || !json.has("ansprechpartner") || !json.has("adresse")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        /**
        JsonObject adresse = json.get("adresse").getAsJsonObject();

        if (!adresse.has("ort") || !adresse.has("hausnummer") || !adresse.has("strasse")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (ortrepository.findByPlz(adresse.get("ort").getAsInt()) == null) {
            Ort ort = new Ort(adresse.get("ort").getAsInt(), adresse.get("name").getAsString());
            ortrepository.save(ort);
        }

        Ort ort = ortrepository.findByPlz(adresse.get("ort").getAsInt());

        if (adresseRepository.findByStrasseAndHausnummerAndOrt(adresse.get("strasse").getAsString(), adresse.get("hausnummer").getAsString(), ort) == null) {
            adresseRepository.save(new Adresse(adresse.get("strasse").getAsString(), adresse.get("hausnummer").getAsString(), ortrepository.findByPlz(adresse.get("ort").getAsInt())));
        }


        Adresse adresse1 = adresseRepository.findByStrasseAndHausnummerAndOrt(adresse.get("strasse").getAsString(), adresse.get("hausnummer").getAsString(), ort);


        Lieferant lieferant = lieferantRepository.findLieferantById(json.get("id").getAsLong());
        if (lieferant == null) {
            return ResponseEntity.badRequest().body("Lieferant nicht vorhanden");
        }

        lieferant.setName(json.get("name").getAsString());
        lieferant.setAdresse(adresse1);
        lieferant.setTelefonnummer(json.get("telefonnummer").getAsString());
        lieferant.setAnsprechpartner(json.get("ansprechpartner").getAsString());

        lieferantRepository.save(lieferant);
        return ResponseEntity.ok("Lieferant edited");

         */
        return ResponseEntity.ok("Lieferant edited");
    }



}
