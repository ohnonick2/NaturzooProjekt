package net.ohnonick2.naturzooprojekt.backend.gebaeude;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.service.GebaeudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/gebaeude")
public class GebaeudeManager {


    @Autowired
    private GebaeudeService gebaeudeService;



    @PostMapping("/delete")
    public ResponseEntity<String> deleteGebaeude(@RequestBody String body) {
        if (JsonParser.parseString(body).getAsJsonObject() == null) {
            return ResponseEntity.badRequest().build();
        }
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (gebaeudeService.deleteGebaeude(json.get("id").getAsLong())) {
            return ResponseEntity.ok("Gebäude erfolgreich gelöscht.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addGebaeude(@RequestBody String body) {

        if (JsonParser.parseString(body).getAsJsonObject() == null) {
            return ResponseEntity.badRequest().build();
        }

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json.get("beschreibung").getAsString().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        if (gebaeudeService.createGebaeude(json.get("name").getAsString(), json.get("beschreibung").getAsString(), json.get("maximaleKapazitaet").getAsInt())) {
            return ResponseEntity.ok("Gebäude erfolgreich erstellt.");
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    @PostMapping("/edit")
    public ResponseEntity<String> editGebaeude(@RequestBody String body) {

        if (JsonParser.parseString(body).getAsJsonObject() == null) {
            return ResponseEntity.badRequest().build();
        }

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json.get("beschreibung").getAsString().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        if (gebaeudeService.updateGebaeude(json.get("id").getAsLong(), json.get("name").getAsString(), json.get("beschreibung").getAsString(), json.get("maximaleKapazitaet").getAsInt())) {
            return ResponseEntity.ok("Gebäude erfolgreich aktualisiert.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addTier")
    public ResponseEntity<String> addTierToGebaeude(@RequestBody String body) {
        if (JsonParser.parseString(body).getAsJsonObject() == null) {
            return ResponseEntity.badRequest().build();
        }

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (gebaeudeService.addTierToGebaeude(json.get("gebaeudeId").getAsLong(), json.get("tierId").getAsLong())) {
            return ResponseEntity.ok("Tier erfolgreich hinzugefügt.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/removeTier")
    public ResponseEntity<String> removeTierFromGebaeude(@RequestBody String body) {
        if (JsonParser.parseString(body).getAsJsonObject() == null) {
            return ResponseEntity.badRequest().build();
        }

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (gebaeudeService.removeTierFromGebaeude(json.get("gebaeudeId").getAsLong(), json.get("tierId").getAsLong())) {
            return ResponseEntity.ok("Tier erfolgreich entfernt.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }






}
