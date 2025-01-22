package net.ohnonick2.naturzooprojekt.backend.food;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.futter.Futter;

import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;

import net.ohnonick2.naturzooprojekt.repository.FutterRepositority;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/food")
public class FoodManager {

    @Autowired
    private FutterRepositority futterRepository;

    @Autowired
    private LieferantRepository lieferantRepository;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addFood(@RequestBody String food) {

        if (!food.startsWith("{") && !food.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültiges JSON-Objekt");
        }

        JsonObject foodJson = new JsonParser().parse(food).getAsJsonObject();
        String name = foodJson.get("name").getAsString();
        double menge = foodJson.get("menge").getAsDouble();
        Long lieferantId = foodJson.get("lieferantId").getAsLong();

        Futter futter = futterRepository.findFutterByName(name);
        if (futter != null) {
            return ResponseEntity.badRequest().body("Futter existiert bereits");
        }



        // Prüfe, ob der Lieferant existiert
        Lieferant lieferant = lieferantRepository.findById(lieferantId
        ).orElse(null);
        if (lieferant == null) {
            return ResponseEntity.badRequest().body("Lieferant nicht gefunden");
        }

        // Erstelle ein neues Futter-Objekt
        futter = new Futter();
        futter.setName(name);
        futter.setMenge(menge);
        futter.setLieferant(lieferant);

        // Speichere das Futter
        futterRepository.save(futter);

        return ResponseEntity.ok("Futter erfolgreich hinzugefügt");

    }

    @PostMapping(value = "/edit")
    public ResponseEntity<String> editFood(@RequestBody String food) {
       if (!food.startsWith("{") && !food.endsWith("}")) {
            return ResponseEntity.badRequest().body("Ungültiges JSON-Objekt");
        }

        JsonObject foodJson = new JsonParser().parse(food).getAsJsonObject();
        Long id = foodJson.get("id").getAsLong();
        String name = foodJson.get("name").getAsString();
        double menge = foodJson.get("menge").getAsDouble();
        Long lieferantId = foodJson.get("lieferantId").getAsLong();

        // Lade das vorhandene Futter-Objekt
        Futter existingFutter = futterRepository.findById(id).orElse(null);
        if (existingFutter == null) {
            return ResponseEntity.badRequest().body("Futter nicht gefunden");
        }

        // Prüfe, ob der Lieferant existiert
        Lieferant lieferant = lieferantRepository.findById(lieferantId).orElse(null);
        if (lieferant == null) {
            return ResponseEntity.badRequest().body("Lieferant nicht gefunden");
        }

        // Aktualisiere die Felder
        existingFutter.setName(name);
        existingFutter.setMenge(menge);
        existingFutter.setLieferant(lieferant);

        // Speichere die Änderungen
        futterRepository.save(existingFutter);

        return ResponseEntity.ok("Futter erfolgreich bearbeitet");
    }
}
