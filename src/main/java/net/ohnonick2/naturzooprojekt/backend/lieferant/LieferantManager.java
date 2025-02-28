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
                return ResponseEntity.badRequest().body("Ungültiges JSON-Format.");
            }

            System.out.println(json);

            // Prüfen, ob alle erforderlichen Felder vorhanden sind
            if (!json.has("name") || !json.has("telefon")) {
                return ResponseEntity.badRequest().body("Fehlende Attribute: Name oder Telefon.");
            }

            String name = json.get("name").getAsString();
            String telefon = json.get("telefon").getAsString();
            String ansprechpartner = json.has("ansprechpartner") ? json.get("ansprechpartner").getAsString() : null;

            // Überprüfung, ob der Lieferant bereits existiert
            if (lieferantRepository.existsLieferantByName(name)) {
                return ResponseEntity.badRequest().body("Ein Lieferant mit diesem Namen existiert bereits.");
            }

            Adresse adresse = null;

            // **Falls Adresse als ID angegeben wurde**
            if (json.has("adresse") && json.get("adresse").isJsonObject()) {
                JsonObject adresseJson = json.getAsJsonObject("adresse");

                if (adresseJson.has("id")) {
                    try {
                        long adresseId = adresseJson.get("id").getAsLong();
                        adresse = adresseRepository.findById(adresseId).orElse(null);
                        if (adresse == null) {
                            return ResponseEntity.badRequest().body("Adresse mit ID " + adresseId + " nicht gefunden.");
                        }
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Ungültige Adresse-ID.");
                    }
                }
                // **Falls Adresse als neues Objekt übergeben wurde**
                else if (adresseJson.has("strasse") && adresseJson.has("hausnummer") && adresseJson.has("plz") && adresseJson.has("ortname")) {
                    int plz;
                    try {
                        plz = Integer.parseInt(adresseJson.get("plz").getAsString().trim());
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("PLZ muss eine gültige Zahl sein.");
                    }

                    String ortname = adresseJson.get("ortname").getAsString();
                    Ort ort = ortrepository.findByOrtname(ortname);
                    if (ort == null) {
                        ort = new Ort(plz, ortname);
                        ortrepository.save(ort);
                    }

                    // Adresse abrufen oder neu erstellen
                    adresse = adresseRepository.findByStrasseAndHausnummerAndOrt(
                            adresseJson.get("strasse").getAsString(),
                            adresseJson.get("hausnummer").getAsString(),
                            ort
                    );

                    if (adresse == null) {
                        adresse = new Adresse(
                                adresseJson.get("strasse").getAsString(),
                                adresseJson.get("hausnummer").getAsString(),
                                ort
                        );
                        adresseRepository.save(adresse);
                    }
                } else {
                    return ResponseEntity.badRequest().body("Fehlende Adressinformationen.");
                }
            } else {
                return ResponseEntity.badRequest().body("Es muss eine bestehende oder neue Adresse angegeben werden.");
            }

            // Neuen Lieferanten speichern
            Lieferant lieferant = new Lieferant(name, adresse, telefon, ansprechpartner);
            lieferantRepository.save(lieferant);

            return ResponseEntity.ok("Lieferant erfolgreich hinzugefügt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Speichern des Lieferanten: " + e.getMessage());
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
