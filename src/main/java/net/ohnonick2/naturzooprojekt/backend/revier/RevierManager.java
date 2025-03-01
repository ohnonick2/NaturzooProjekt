package net.ohnonick2.naturzooprojekt.backend.revier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.service.RevierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/revier")
public class RevierManager {

    @Autowired
    private RevierService revierService;


    @PostMapping("/add")
    public ResponseEntity<String> addRevier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();

        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Name darf nicht leer sein.");
        }

        if (revierService.createRevier(name)) {
            return ResponseEntity.ok("Revier erfolgreich erstellt.");
        } else {
            return ResponseEntity.badRequest().body("Revier konnte nicht erstellt werden.");
        }


    }

    // Revier bearbeiten
    @PostMapping("/edit")
    public ResponseEntity<String> updateRevier(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();

        if (revierService.updateRevier(id, name)) {
            return ResponseEntity.ok("Revier erfolgreich bearbeitet.");
        } else {
            return ResponseEntity.badRequest().body("Revier konnte nicht bearbeitet werden.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteRevier(@RequestBody String body) {

        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long id = jsonObject.get("id").getAsLong();

        if (revierService.deleteRevier(id)) {
            return ResponseEntity.ok("Revier erfolgreich gelöscht.");
        } else {
            return ResponseEntity.badRequest().body("Revier konnte nicht gelöscht werden.");
        }
    }


    // Pfleger zum Revier hinzufügen
    @PostMapping("/addPfleger")
    public ResponseEntity<String> addPfleger(@RequestBody String body) {

        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
        }

        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long pflegerId = jsonObject.get("pflegerId").getAsLong();

        if (revierService.addPflegerToRevier(revierId, pflegerId)) {
            return ResponseEntity.ok("Pfleger erfolgreich hinzugefügt.");
        } else {
            return ResponseEntity.badRequest().body("Pfleger konnte nicht hinzugefügt werden.");
        }

    }

    // Pfleger aus Revier entfernen
    @PostMapping("/removePfleger")
    public ResponseEntity<String> removePfleger(@RequestBody String body) {

            if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
                return ResponseEntity.badRequest().body("Ungültige JSON-Daten.");
            }

            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
            Long revierId = jsonObject.get("revierId").getAsLong();
            Long pflegerId = jsonObject.get("pflegerId").getAsLong();

            if (revierService.removePflegerFromRevier(revierId, pflegerId)) {
                return ResponseEntity.ok("Pfleger erfolgreich entfernt.");
            } else {
                return ResponseEntity.badRequest().body("Pfleger konnte nicht entfernt werden.");
            }
    }

}
