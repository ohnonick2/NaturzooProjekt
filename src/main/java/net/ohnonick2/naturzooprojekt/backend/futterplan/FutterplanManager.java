package net.ohnonick2.naturzooprojekt.backend.futterplan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Autowired
    private Tierrespository tierrespository;
    @Autowired
    private FutterPlanTierRepository futterPlanTierRepositority;
    @Autowired
    private RevierTierRepository revierTierRepository;
    @Autowired
    private RevierRepository revierRepository;

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


        for (JsonElement wochentagElement : wochentageArray) {
            Long wochentagId = wochentagElement.getAsLong();
            Wochentag wochentag = wochenTagRepository.findById(wochentagId).orElse(null);

            if (wochentag == null) {
                return ResponseEntity.badRequest().body("Ungültiger Wochentag: " + wochentagId);
            }

            futterPlanWochentagRepository.save(new FutterPlanWochentag(futterPlan, wochentag));
        }


        for (JsonElement uhrzeitElement : uhrzeitenArray) {
            String uhrzeit = uhrzeitElement.getAsString().trim();

            if (uhrzeit.isEmpty() || uhrzeit.equalsIgnoreCase("x")) {
                continue; // Überspringe leere oder ungültige Uhrzeiten
            }

            FutterZeit futterZeit = futterZeitRepository.findFutterZeitByuhrzeit(uhrzeit);

            if (futterZeit == null) {
                futterZeit = new FutterZeit(uhrzeit);
                futterZeitRepository.save(futterZeit);
            }

            futterPlanFutterZeitRepository.save(new FutterPlanFutterZeit(futterZeit, futterPlan));
        }

        // ✅ Futter speichern (null-Werte ignorieren)
        for (JsonElement futterElement : futterListArray) {
            JsonObject futterObj = futterElement.getAsJsonObject();
            if (!futterObj.has("futterId") || futterObj.get("futterId").isJsonNull()) {
                continue; // Überspringe ungültige Einträge
            }

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

        // ✅ Tiere speichern (wenn vorhanden)
        if (json.has("tiere")) {
            JsonArray tierArray = json.getAsJsonArray("tiere");
            for (JsonElement tierElement : tierArray) {
                Long tierId = tierElement.getAsLong();
                Tier tier = tierrespository.findById(tierId).orElse(null);
                if (tier == null) {
                    return ResponseEntity.badRequest().body("Tier mit ID " + tierId + " nicht gefunden.");
                }
                futterPlanTierRepositority.save(new FutterPlanTier(futterPlan, tier));
            }
        }

        // ✅ Revier speichern (wenn vorhanden)
        if (json.has("revierId") && !json.get("revierId").isJsonNull()) {
            Long revierId = json.get("revierId").getAsLong();

            Revier revier = revierRepository.findById(revierId).orElse(null);
            if (revier == null) {
                return ResponseEntity.badRequest().body("Revier mit ID " + revierId + " nicht gefunden.");
            }

            List<RevierTier> revierTierList = revierTierRepository.findAllByRevierId(revier);

            for (RevierTier revierTier : revierTierList) {
                futterPlanTierRepositority.save(new FutterPlanTier(futterPlan, revierTier.getTierId()));
            }
        }

        return ResponseEntity.ok("Futterplan erfolgreich mit Wochentagen, Uhrzeiten und Futter hinzugefügt.");
    }


    @PostMapping(value = "/edit")
    public ResponseEntity<String> editFutterplan(@RequestBody String body) {
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            if (!json.has("id") || !json.has("name") || !json.has("wochentage") || !json.has("uhrzeiten") || !json.has("futterList")) {
                return ResponseEntity.badRequest().body("Fehlende Pflichtfelder: 'id', 'name', 'wochentage', 'uhrzeiten', 'futterList'");
            }

            Long futterplanId = json.get("id").getAsLong();
            String futterplanName = json.get("name").getAsString();
            JsonArray wochentageArray = json.getAsJsonArray("wochentage");
            JsonArray uhrzeitenArray = json.getAsJsonArray("uhrzeiten");
            JsonArray futterListArray = json.getAsJsonArray("futterList");

            Optional<FutterPlan> optionalFutterPlan = futterPlanRepository.findById(futterplanId);
            if (optionalFutterPlan.isEmpty()) {
                return ResponseEntity.badRequest().body("Futterplan mit ID " + futterplanId + " nicht gefunden.");
            }

            FutterPlan futterPlan = optionalFutterPlan.get();

            // 📝 Name aktualisieren
            futterPlan.setName(futterplanName);

            // 🗑️ Entferne nicht mehr existierende Wochentage
            List<FutterPlanWochentag> alteWochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan);
            Set<Long> neueWochentageIds = new HashSet<>();
            for (JsonElement wochentagElement : wochentageArray) {
                neueWochentageIds.add(wochentagElement.getAsLong());
            }

            alteWochentage.stream()
                    .filter(w -> !neueWochentageIds.contains(w.getWochentag().getId()))
                    .forEach(futterPlanWochentagRepository::delete);

            // 📝 Neue Wochentage hinzufügen
            for (Long wochentagId : neueWochentageIds) {
                Wochentag wochentag = wochenTagRepository.findById(wochentagId).orElse(null);
                if (wochentag != null && alteWochentage.stream().noneMatch(w -> w.getWochentag().getId() == wochentagId)) {
                    futterPlanWochentagRepository.save(new FutterPlanWochentag(futterPlan, wochentag));
                }
            }

            // 🕑 Uhrzeiten aktualisieren
            List<FutterPlanFutterZeit> alteUhrzeiten = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId());
            Set<String> neueUhrzeiten = new HashSet<>();
            for (JsonElement uhrzeitElement : uhrzeitenArray) {
                neueUhrzeiten.add(uhrzeitElement.getAsString().trim());
            }

            alteUhrzeiten.stream()
                    .filter(z -> !neueUhrzeiten.contains(z.getFutterZeit().getUhrzeit()))
                    .forEach(futterPlanFutterZeitRepository::delete);

            for (String uhrzeit : neueUhrzeiten) {
                if (!uhrzeit.isEmpty()) {
                    FutterZeit futterZeit = futterZeitRepository.findFutterZeitByuhrzeit(uhrzeit);
                    if (futterZeit == null) {
                        futterZeit = new FutterZeit(uhrzeit);
                        futterZeitRepository.save(futterZeit);
                    }
                    futterPlanFutterZeitRepository.save(new FutterPlanFutterZeit(futterZeit, futterPlan));
                }
            }

            // 🍽️ Futter aktualisieren
            List<FutterplanFutter> alteFutter = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());

            // 🛠️ JSON-Array in eine Liste von IDs umwandeln
            Set<Long> neueFutterIds = new HashSet<>();
            Map<Long, Integer> neueFutterMengen = new HashMap<>();
            for (JsonElement futterElement : futterListArray) {
                JsonObject futterObj = futterElement.getAsJsonObject();
                if (futterObj.has("futterId") && !futterObj.get("futterId").isJsonNull()) {
                    Long futterId = futterObj.get("futterId").getAsLong();
                    int menge = futterObj.get("menge").getAsInt();
                    neueFutterIds.add(futterId);
                    neueFutterMengen.put(futterId, menge);
                }
            }

            // 🗑️ Lösche alte Futtereinträge, die nicht mehr existieren
            for (FutterplanFutter f : alteFutter) {
                if (!neueFutterIds.contains(f.getFutter().getId())) {
                    futterplanFutterRepository.delete(f);
                }
            }

            // ✨ Neue Futtereinträge hinzufügen
            for (Long futterId : neueFutterIds) {
                Futter futter = futterRepository.findById(futterId).orElse(null);
                if (futter != null) {
                    FutterplanFutter futterplanFutter = new FutterplanFutter(futterPlan, futter, neueFutterMengen.get(futterId));
                    futterplanFutterRepository.save(futterplanFutter);
                }
            }

            futterPlanRepository.save(futterPlan);
            return ResponseEntity.ok("Futterplan erfolgreich aktualisiert.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ungültige JSON-Daten: " + e.getMessage());
        }
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
        futterPlanFutterZeitRepository.deleteAll(futterPlanFutterZeit);

        List<FutterPlanWochentag> futterPlanWochentag = futterPlanWochentagRepository.findByFutterplan(futterPlan);
        futterPlanWochentagRepository.deleteAll(futterPlanWochentag);

        List<FutterplanFutter> futterplanFutters = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());
        futterplanFutterRepository.deleteAll(futterplanFutters);







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
