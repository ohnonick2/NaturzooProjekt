package net.ohnonick2.naturzooprojekt.backend.tierart;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import net.ohnonick2.naturzooprojekt.repository.Tierartrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/tierart")
public class TierartManager {

    @Autowired
    private Tierartrepository tierartrepository;

    @PostMapping("/add")
    public ResponseEntity<String> addTierart(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (!jsonObject.has("name")) {
            return ResponseEntity.badRequest().body("Fehlende Felder im JSON.");
        }

        String name = jsonObject.get("name").getAsString();

        if (tierartrepository.findByName(name) != null) {
            return ResponseEntity.badRequest().body("Tierart existiert bereits!");
        }

        TierArt tierArt = new TierArt(name);

        tierartrepository.save(tierArt);

        return ResponseEntity.ok("Tierart erfolgreich hinzugefügt.");



    }

    @PostMapping("/edit")
    public ResponseEntity<String> editTierart(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (!jsonObject.has("name") || !jsonObject.has("newName")) {
            return ResponseEntity.badRequest().body("Fehlende Felder im JSON.");
        }

        String name = jsonObject.get("name").getAsString();
        String newName = jsonObject.get("newName").getAsString();

        TierArt tierArt = tierartrepository.findByName(name);

        if (tierArt == null) {
            return ResponseEntity.badRequest().body("Tierart existiert nicht.");
        }

        tierArt.setName(newName);

        tierartrepository.save(tierArt);

        return ResponseEntity.ok("Tierart erfolgreich bearbeitet.");
    }



    @PostMapping("/delete")
    public ResponseEntity<String> deleteTierart(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (!jsonObject.has("name")) {
            return ResponseEntity.badRequest().body("Fehlende Felder im JSON.");
        }

        String name = jsonObject.get("name").getAsString();

        TierArt tierArt = tierartrepository.findByName(name);

        if (tierArt == null) {
            return ResponseEntity.badRequest().body("Tierart existiert nicht.");
        }

        tierartrepository.delete(tierArt);

        return ResponseEntity.ok("Tierart erfolgreich gelöscht.");
    }


}
