package net.ohnonick2.naturzooprojekt.backend.futterplan;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/futterplan")
public class FutterplanManager {

    @Autowired
    private FutterPlanRepository futterPlanRepository;
    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;
    @Autowired
    private FutterplanFutterRepository futterplanFutterRepository;
    @Autowired
    private FutterRepository futterRepository;
    @Autowired
    private FutterZeitRepository futterZeitRepository;

    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addFutterplan(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null || !json.has("name")) {
            return ResponseEntity.badRequest().body("Invalid JSON or missing 'name' field.");
        }

        String futterplanName = json.get("name").getAsString();
        FutterPlan futterPlan = new FutterPlan(futterplanName);
        futterPlanRepository.save(futterPlan);

        return ResponseEntity.ok("Futterplan added successfully.");
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<String> deleteFutterplan(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null || !json.has("futterplanId")) {
            return ResponseEntity.badRequest().body("Invalid JSON or missing 'futterplanId' field.");
        }

        Long futterplanId = json.get("futterplanId").getAsLong();

        // Überprüfen, ob der Futterplan existiert
        FutterPlan futterPlan = futterPlanRepository.findById(futterplanId).orElse(null);
        if (futterPlan == null) {
            return ResponseEntity.badRequest().body("Futterplan with ID " + futterplanId + " not found.");
        }

        // Löschen der Zuordnungen von Wochentagen


        // Löschen des Futterplans
        futterPlanRepository.deleteById(futterplanId);

        return ResponseEntity.ok("Futterplan with ID " + futterplanId + " deleted successfully.");
    }


    @PostMapping(value = "/addFeedingTime")
    public ResponseEntity<String> addFeedingTime(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null || !json.has("futterplanId") || !json.has("time")) {
            return ResponseEntity.badRequest().body("Invalid JSON or missing required fields.");
        }

        Long futterplanId = json.get("futterplanId").getAsLong();
        String feedingTime = json.get("time").getAsString();


        if (futterPlanRepository.findById(futterplanId).isEmpty()) {
            return ResponseEntity.badRequest().body("Futterplan with ID " + futterplanId + " not found.");
        }

        FutterZeit futterZeit = new FutterZeit(feedingTime);

        if (futterZeitRepository.findFutterZeitByuhrzeit(feedingTime) == null) {

            futterZeitRepository.save(futterZeit);
            return ResponseEntity.badRequest().body("Feeding time already exists.");
        }

        FutterPlan futterPlan = futterPlanRepository.findById(futterplanId).get();
        if (futterPlan == null) {
            return ResponseEntity.badRequest().body("Futterplan with ID " + futterplanId + " not found.");
        }

        FutterPlanFutterZeit futterPlanFutterZeit = new FutterPlanFutterZeit(futterZeit , futterPlan);

        futterPlanFutterZeitRepository.save(futterPlanFutterZeit);

        return ResponseEntity.ok("Feeding time added successfully.");
    }
}
