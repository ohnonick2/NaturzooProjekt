package net.ohnonick2.naturzooprojekt.backend.revier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/revier")
public class RevierManager {

    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private RevierTierRepository revierTierRepository;

    @Autowired
    private Pflegerrepository pflegerRepository;

    @Autowired
    private Tierrespository tierRepository;

    // Revier bearbeiten
    @PostMapping("/edit")
    public ResponseEntity<String> updateRevier(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();

        Optional<Revier> revierOptional = revierRepository.findById(id);
        if (revierOptional.isPresent()) {
            Revier revier = revierOptional.get();
            revier.setName(name);
            revierRepository.save(revier);
            return ResponseEntity.ok("Revier erfolgreich bearbeitet!");
        } else {
            return ResponseEntity.badRequest().body("Revier nicht gefunden!");
        }
    }

    // Pfleger zum Revier hinzufügen
    @PostMapping("/addPfleger")
    public ResponseEntity<String> addPfleger(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long pflegerId = jsonObject.get("pflegerId").getAsLong();

        Revier revier = revierRepository.findById(revierId).orElse(null);
        Pfleger pfleger = pflegerRepository.findById(pflegerId).orElse(null);

        if (revier == null || pfleger == null) {
            return ResponseEntity.badRequest().body("Revier oder Pfleger nicht gefunden!");
        }

        RevierPfleger revierPfleger = new RevierPfleger(revier, pfleger);
        revierPflegerRepository.save(revierPfleger);

        return ResponseEntity.ok("Pfleger erfolgreich hinzugefügt!");
    }

    // Pfleger aus Revier entfernen
    @PostMapping("/removePfleger")
    public ResponseEntity<String> removePfleger(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long pflegerId = jsonObject.get("pflegerId").getAsLong();

        Revier revier = revierRepository.findById(revierId).orElse(null);
        Pfleger pfleger = pflegerRepository.findById(pflegerId).orElse(null);
        if (revier == null || pfleger == null) {
            return ResponseEntity.badRequest().body("Revier oder Pfleger nicht gefunden!");
        }


        RevierPfleger revierPfleger = revierPflegerRepository.findByRevierAndPfleger(revier, pfleger);
        if (revierPfleger == null) {
            return ResponseEntity.badRequest().body("Pfleger nicht im Revier gefunden!");
        }

        revierPflegerRepository.delete(revierPfleger);
        return ResponseEntity.ok("Pfleger erfolgreich entfernt!");
    }

    // Tier zum Revier hinzufügen
    @PostMapping("/addTier")
    public ResponseEntity<String> addTierToRevier(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long tierId = jsonObject.get("tierId").getAsLong();

        Revier revier = revierRepository.findById(revierId).orElse(null);
        Tier tier = tierRepository.findById(tierId).orElse(null);

        if (revier == null || tier == null) {
            return ResponseEntity.badRequest().body("Revier oder Tier nicht gefunden!");
        }

        RevierTier revierTier = new RevierTier(revier, tier);
        revierTierRepository.save(revierTier);

        return ResponseEntity.ok("Tier erfolgreich zum Revier hinzugefügt!");
    }

    // Tier aus Revier entfernen
    @PostMapping("/removeTier")
    public ResponseEntity<String> removeTierFromRevier(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long tierId = jsonObject.get("tierId").getAsLong();

        Revier revier = revierRepository.findById(revierId).orElse(null);
        Tier tier = tierRepository.findById(tierId).orElse(null);
        if (revier == null || tier == null) {
            return ResponseEntity.badRequest().body("Revier oder Tier nicht gefunden!");
        }


        RevierTier revierTier = revierTierRepository.findByRevierIdAndTierId(revier, tier);
        if (revierTier == null) {
            return ResponseEntity.badRequest().body("Das Tier ist nicht in diesem Revier!");
        }

        revierTierRepository.delete(revierTier);
        return ResponseEntity.ok("Tier erfolgreich aus dem Revier entfernt!");
    }
}
