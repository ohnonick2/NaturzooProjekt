package net.ohnonick2.naturzooprojekt.backend.futterplan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private WochenTagRepository wochenTagRepository;

    @Autowired
    private FutterZeitRepository futterzeitRepository;


    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addFutterplan(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        System.out.println(json);
        if (!json.has("name") || !json.has("wochentage") || !json.has("uhrzeiten") || !json.has("futterList")) {
            return ResponseEntity.badRequest().body("Fehlende Felder: 'name', 'wochentage', 'uhrzeiten' oder 'futterList'");
        }

        String futterplanName = json.get("name").getAsString();
        JsonArray wochentageArray = json.getAsJsonArray("wochentage");
        JsonArray uhrzeitenArray = json.getAsJsonArray("uhrzeiten");
        JsonArray futterListArray = json.getAsJsonArray("futterList");

        FutterPlan futterPlan = new FutterPlan(futterplanName);
        futterPlanRepository.save(futterPlan);

        // ✅ Wochentage speichern
        for (int i = 0; i < wochentageArray.size(); i++) {
            Long wochentagId = wochentageArray.get(i).getAsLong();
            Wochentag wochentag = wochenTagRepository.findById(wochentagId).orElse(null);

            if (wochentag == null) {
                return ResponseEntity.badRequest().body("Ungültiger Wochentag: " + wochentagId);
            }

            futterPlanWochentagRepository.save(new FutterPlanWochentag(futterPlan, wochentag));
        }

        // ✅ Uhrzeiten speichern
        for (int i = 0; i < uhrzeitenArray.size(); i++) {
            String uhrzeit = uhrzeitenArray.get(i).getAsString();

            //remove * als uhrzeit from the list
            if (uhrzeit.equals("x")) {
                continue;
            }

            FutterZeit futterZeit = futterZeitRepository.findFutterZeitByuhrzeit(uhrzeit);

            if (futterZeit == null) {
                futterZeit = new FutterZeit(uhrzeit);
                futterZeitRepository.save(futterZeit);
            }

            futterPlanFutterZeitRepository.save(new FutterPlanFutterZeit(futterZeit, futterPlan));


        }


        for (int i = 0; i < futterListArray.size(); i++) {
            JsonObject futterObj = futterListArray.get(i).getAsJsonObject();
            Long futterId = futterObj.get("futterId").getAsLong();
            int menge = futterObj.get("menge").getAsInt();

            if (menge <= 0) {
                return ResponseEntity.badRequest().body("Ungültige Menge für Futter-ID: " + futterId);
            }

            Futter futter = futterRepository.findById(futterId).orElse(null);
            if (futter == null) {
                return ResponseEntity.badRequest().body("Ungültiges Futter mit ID: " + futterId);
            }

            futterplanFutterRepository.save(new FutterplanFutter(futterPlan, futter, menge));
        }

        return ResponseEntity.ok("Futterplan erfolgreich mit Wochentagen, Uhrzeiten und Futter hinzugefügt.");
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

        List<FutterPlanFutterZeit> futterPlanFutterZeit = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId());
        for (FutterPlanFutterZeit planFutterZeit : futterPlanFutterZeit) {
            futterPlanFutterZeitRepository.delete(planFutterZeit);
        }

        List<FutterPlanWochentag> futterPlanWochentag = futterPlanWochentagRepository.findByFutterplan(futterPlan);
        for (FutterPlanWochentag planWochentag : futterPlanWochentag) {
            futterPlanWochentagRepository.delete(planWochentag);
        }

        List<FutterplanFutter> futterplanFutters = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());
        for (FutterplanFutter futterplanFutter : futterplanFutters) {
            futterplanFutterRepository.delete(futterplanFutter);
        }







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

    @PostMapping(value = "/deleteFeedingTime")
    public ResponseEntity<String> deleteFeedingTime(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null || !json.has("futterplanId") || !json.has("time")) {
            return ResponseEntity.badRequest().body("Invalid JSON or missing required fields.");
        }

        Long futterplanId = json.get("futterplanId").getAsLong();
        String feedingTime = json.get("time").getAsString();

        FutterZeit futterZeit = futterZeitRepository.findFutterZeitByuhrzeit(feedingTime);
        if (futterZeit == null) {
            return ResponseEntity.badRequest().body("Feeding time with time " + feedingTime + " not found.");
        }

        FutterPlan futterPlan = futterPlanRepository.findById(futterplanId).orElse(null);
        if (futterPlan == null) {
            return ResponseEntity.badRequest().body("Futterplan with ID " + futterplanId + " not found.");
        }

        List<FutterPlanFutterZeit> futterPlanFutterZeit = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId());
        if (futterPlanFutterZeit == null) {
            return ResponseEntity.badRequest().body("Feeding time with time " + feedingTime + " not found in Futterplan with ID " + futterplanId + ".");
        }

        for (FutterPlanFutterZeit planFutterZeit : futterPlanFutterZeit) {
            if (planFutterZeit.getFutterZeit().getUhrzeit().equals(feedingTime)) {
                futterPlanFutterZeitRepository.delete(planFutterZeit);
            }
        }

        return ResponseEntity.ok("Feeding time with time " + feedingTime + " deleted successfully.");
    }

    @PostMapping(value = "/addFeeding")
    public ResponseEntity<String> addFeeding(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null || !json.has("futterplanId") || !json.has("futterId") || !json.has("menge")) {
            return ResponseEntity.badRequest().body("Invalid JSON or missing required fields.");
        }

        Long futterplanId = json.get("futterplanId").getAsLong();
        Long futterId = json.get("futterId").getAsLong();
        int menge = json.get("menge").getAsInt();

        FutterPlan futterPlan = futterPlanRepository.findById(futterplanId).orElse(null);
        if (futterPlan == null) {
            return ResponseEntity.badRequest().body("Futterplan with ID " + futterplanId + " not found.");
        }

        Futter futter = futterRepository.findById(futterId).orElse(null);
        if (futter == null) {
            return ResponseEntity.badRequest().body("Futter with ID " + futterId + " not found.");
        }

        FutterplanFutter futterplanFutter = new FutterplanFutter(futterPlan, futter, menge);
        futterplanFutterRepository.save(futterplanFutter);

        return ResponseEntity.ok("Feeding added successfully.");
    }


}
