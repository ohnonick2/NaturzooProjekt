package net.ohnonick2.naturzooprojekt.backend.tier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tier")
public class TierManager {

    @Autowired
    private Tierrespository tierrespository;

    @Autowired
    private Tierartrepository tierartrepository;

    @Autowired
    private FutterPlanTierRepository futterPlanTierRepositority;

    @Autowired
    private FutterPlanRepository futterPlanRepository;

    @Autowired
    private RevierRepository revierRepository;


    @PostMapping("/add")
    public ResponseEntity<String> addTier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(body).getAsJsonObject();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Parsen der JSON-Daten: " + e.getMessage());
        }

        // Prüfen, ob alle erforderlichen Felder existieren
        if (!jsonObject.has("name") || !jsonObject.has("geburtsdatum") || !jsonObject.has("geschlecht")
                || !jsonObject.has("tierArt")) {
            return ResponseEntity.badRequest().body("Fehlende Felder im JSON.");
        }

        // 🔹 Name sicher extrahieren
        String name = jsonObject.get("name").getAsString();

        // 🔹 Geburtsdatum sicher extrahieren
        LocalDate geburtsdatum;
        try {
            geburtsdatum = LocalDate.parse(jsonObject.get("geburtsdatum").getAsString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ungültiges Geburtsdatum-Format.");
        }

        // 🔹 Sterbedatum prüfen und sicher extrahieren
        LocalDate sterbedatum = null;
        if (jsonObject.has("sterbedatum") && !jsonObject.get("sterbedatum").isJsonNull()) {
            try {
                sterbedatum = LocalDate.parse(jsonObject.get("sterbedatum").getAsString());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ungültiges Sterbedatum-Format.");
            }
        }

        // 🔹 Geschlecht sicher extrahieren
        TierGeschlecht geschlecht;
        try {
            geschlecht = TierGeschlecht.valueOf(jsonObject.get("geschlecht").getAsString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ungültiges Geschlecht.");
        }

        // 🔹 Prüfen, ob die Tierart existiert – wenn nicht, wird sie erstellt
        String tierArtName = jsonObject.get("tierArt").getAsString().trim();
        TierArt tierArt = tierartrepository.findByName(tierArtName);
        if (tierArt == null) {
            TierArt newTierArt = new TierArt();
            newTierArt.setName(tierArtName);
            TierArt save = tierartrepository.save(newTierArt);
            tierArt = save;
        }

        // 🔹 Neues Tier erstellen
        Tier tier = new Tier(name, geburtsdatum, sterbedatum, null, geschlecht, tierArt);

        // 🔹 `isAbgegeben` sicher abrufen
        boolean isAbgegeben = jsonObject.has("isAbgegeben") && jsonObject.get("isAbgegeben").getAsBoolean();
        tier.setAbgegeben(isAbgegeben);

        // 🔹 Abgabedatum setzen (falls vorhanden)
        LocalDate abgabeDatum = null;
        if (jsonObject.has("abgabeDatum") && !jsonObject.get("abgabeDatum").isJsonNull()) {
            try {
                abgabeDatum = LocalDate.parse(jsonObject.get("abgabeDatum").getAsString());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ungültiges Abgabedatum-Format.");
            }
        }
        tier.setAbgabeDatum(abgabeDatum);

        // 🔹 Tier in die Datenbank speichern
        tierrespository.save(tier);

        // 🔹 Verbindung zwischen Revier und Tier speichern


        return ResponseEntity.ok("Tier erfolgreich hinzugefügt.");
    }



    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTiere() {
        List<Tier> tiere = tierrespository.findAll();

        if (tiere.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        StringWriter writer = new StringWriter();
        PrintWriter csvWriter = new PrintWriter(writer);

        // CSV-Header
        csvWriter.println("ID,Name,Geburtsdatum,Sterbedatum,Abgegeben,Abgabedatum");

        // Daten in CSV schreiben
        for (Tier tier : tiere) {
            csvWriter.println(
                    tier.getId() + "," +
                            tier.getName() + "," +
                            tier.getGeburtsdatum() + "," +
                            (tier.getSterbedatum() != null ? tier.getSterbedatum() : "") + "," +
                            (tier.isAbgegeben() ? "Ja" : "Nein") + "," +
                            (tier.getAbgabeDatum() != null ? tier.getAbgabeDatum() : "")
            );
        }

        csvWriter.flush();
        byte[] csvBytes = writer.toString().getBytes();

        // CSV als Download bereitstellen
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tiere_export.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvBytes);
    }



    @PostMapping("/delete")
    public ResponseEntity<String> deleteTier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().build();
        }
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        long id = jsonObject.get("id").getAsLong();
        if (id == 0) {
            return ResponseEntity.badRequest().build();
        }
        tierrespository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<String> updateTier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        LocalDate geburtsdatum = LocalDate.parse(jsonObject.get("geburtsdatum").getAsString());
        LocalDate sterbedatum = jsonObject.has("sterbedatum") && !jsonObject.get("sterbedatum").isJsonNull()
                ? LocalDate.parse(jsonObject.get("sterbedatum").getAsString())
                : null;

        TierGeschlecht geschlecht = TierGeschlecht.valueOf(jsonObject.get("geschlecht").getAsString());
        TierArt tierArt = tierartrepository.findById(jsonObject.get("tierArt").getAsLong()).orElse(null);

        if (name == null || geburtsdatum == null || geschlecht == null || tierArt == null) {
            return ResponseEntity.badRequest().body("Fehlende oder ungültige Daten.");
        }

        Tier tier = tierrespository.findById(id);
        if (tier == null) {
            return ResponseEntity.badRequest().body("Tier nicht gefunden.");
        }

        tier.setName(name);
        tier.setGeburtsdatum(geburtsdatum);
        tier.setSterbedatum(sterbedatum);
        tier.setGeschlecht(geschlecht);
        tier.setTierArt(tierArt);

        boolean isAbgegeben = jsonObject.get("isAbgegeben").getAsBoolean();
        tier.setAbgegeben(isAbgegeben);

        if (isAbgegeben) {
            LocalDate abgabeDatum = jsonObject.has("abgabeDatum") && !jsonObject.get("abgabeDatum").isJsonNull()
                    ? LocalDate.parse(jsonObject.get("abgabeDatum").getAsString())
                    : LocalDate.now();  // Falls kein Datum übergeben wird, aktuelles Datum setzen

            tier.setAbgabeDatum(abgabeDatum);
        } else {
            tier.setAbgabeDatum(null);  // Falls das Tier nicht abgegeben ist, Datum entfernen
        }

        tierrespository.save(tier);
        return ResponseEntity.ok("Tier erfolgreich aktualisiert.");
    }



}
