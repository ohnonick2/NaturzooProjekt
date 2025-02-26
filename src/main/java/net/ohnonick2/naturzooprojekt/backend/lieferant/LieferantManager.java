package net.ohnonick2.naturzooprojekt.backend.lieferant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.adresse.Adresse;
import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.repository.AdresseRepository;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Fügt einen neuen Lieferanten hinzu, mit entweder einer bestehenden oder neuen Adresse.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addLieferant(@RequestBody String body) {
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            if (json == null) {
                return ResponseEntity.badRequest().body("Invalid JSON");
            }

            System.out.println(json);

            // Prüfen, ob notwendige Felder vorhanden sind
            if (!json.has("name") || !json.has("telefon")) {
                return ResponseEntity.badRequest().body("Fehlende Attribute: Name oder Telefon.");
            }

            // Ansprechpartner ist optional
            String ansprechpartner = json.has("ansprechpartner") ? json.get("ansprechpartner").getAsString() : null;

            Adresse adresse;
            if (json.has("adresseId")) {
                long adresseId = json.get("adresseId").getAsLong();
                adresse = adresseRepository.findById(adresseId).orElse(null);
                if (adresse == null) {
                    return ResponseEntity.badRequest().body("Adresse mit ID " + adresseId + " nicht gefunden.");
                }
            } else if (json.has("adresse")) {
                JsonObject adresseJson = json.get("adresse").getAsJsonObject();
                if (!adresseJson.has("strasse") || !adresseJson.has("hausnummer") || !adresseJson.has("plz") || !adresseJson.has("ortname")) {
                    return ResponseEntity.badRequest().body("Fehlende Adressinformationen.");
                }

                // Fehler abfangen: PLZ muss eine gültige Zahl sein
                int plz;
                try {
                    plz = Integer.parseInt(adresseJson.get("plz").getAsString());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("PLZ muss eine gültige Zahl sein.");
                }

                String ortname = adresseJson.get("ortname").getAsString();
                Ort ort = ortrepository.findByPlz(plz);
                if (ort == null) {
                    ort = new Ort(plz, ortname);
                    ortrepository.save(ort);
                }

                adresse = adresseRepository.findByStrasseAndHausnummerAndOrt(
                        adresseJson.get("strasse").getAsString(),
                        adresseJson.get("hausnummer").getAsString(),
                        ort
                );

                if (adresse == null) {
                    adresse = new Adresse(adresseJson.get("strasse").getAsString(),
                            adresseJson.get("hausnummer").getAsString(), ort);
                    adresseRepository.save(adresse);
                }
            } else {
                return ResponseEntity.badRequest().body("Es muss eine bestehende oder neue Adresse angegeben werden.");
            }

            // Lieferant speichern
            Lieferant lieferant = new Lieferant(
                    json.get("name").getAsString(),
                    adresse,
                    json.get("telefon").getAsString(),
                    ansprechpartner  // Kann null sein
            );
            lieferantRepository.save(lieferant);

            return ResponseEntity.ok("Lieferant erfolgreich hinzugefügt!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler: " + e.getMessage());
        }
    }


    /**
     * Bearbeitet einen vorhandenen Lieferanten.
     */
    @PostMapping("/edit")
    public ResponseEntity<String> editLieferant(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json == null || !json.has("id") || !json.has("name") || !json.has("telefon") || !json.has("ansprechpartner")) {
            return ResponseEntity.badRequest().body("Fehlende oder ungültige JSON-Daten.");
        }

        long lieferantId = json.get("id").getAsLong();
        Lieferant lieferant = lieferantRepository.findLieferantById(lieferantId);
        if (lieferant == null) {
            return ResponseEntity.badRequest().body("Lieferant nicht vorhanden.");
        }

        Adresse adresse;
        if (json.has("adresseId")) {
            long adresseId = json.get("adresseId").getAsLong();
            adresse = adresseRepository.findById(adresseId).orElse(null);
            if (adresse == null) {
                return ResponseEntity.badRequest().body("Adresse mit ID " + adresseId + " nicht gefunden.");
            }
        } else if (json.has("adresse")) {
            JsonObject adresseJson = json.get("adresse").getAsJsonObject();
            if (!adresseJson.has("strasse") || !adresseJson.has("hausnummer") || !adresseJson.has("plz") || !adresseJson.has("ortname")) {
                return ResponseEntity.badRequest().body("Fehlende Adressinformationen.");
            }

            int plz;
            try {
                plz = Integer.parseInt(adresseJson.get("plz").getAsString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("PLZ muss eine gültige Zahl sein.");
            }

            String ortname = adresseJson.get("ortname").getAsString();
            Ort ort = ortrepository.findByPlz(plz);
            if (ort == null) {
                ort = new Ort(plz, ortname);
                ortrepository.save(ort);
            }

            adresse = adresseRepository.findByStrasseAndHausnummerAndOrt(
                    adresseJson.get("strasse").getAsString(),
                    adresseJson.get("hausnummer").getAsString(),
                    ort
            );
            if (adresse == null) {
                adresse = new Adresse(adresseJson.get("strasse").getAsString(),
                        adresseJson.get("hausnummer").getAsString(), ort);
                adresseRepository.save(adresse);
            }
        } else {
            return ResponseEntity.badRequest().body("Es muss eine bestehende oder neue Adresse angegeben werden.");
        }

        lieferant.setName(json.get("name").getAsString());
        lieferant.setAdresse(adresse);
        lieferant.setTelefonnummer(json.get("telefon").getAsString());
        lieferant.setAnsprechpartner(json.get("ansprechpartner").getAsString());

        lieferantRepository.save(lieferant);
        return ResponseEntity.ok("Lieferant erfolgreich aktualisiert.");
    }
}
