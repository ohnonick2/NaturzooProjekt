package net.ohnonick2.naturzooprojekt.backend.revier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;

import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.repository.RevierPflegerRepository;
import net.ohnonick2.naturzooprojekt.repository.RevierRepository;
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
    private Pflegerrepository pflegerRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addRevier(@RequestBody Revier newRevier) {
        try {
            revierRepository.save(newRevier);
            return ResponseEntity.ok("Revier erfolgreich hinzugefügt!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Hinzufügen des Reviers: " + e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> updateRevier(@RequestBody String body) {
        System.out.println("Received Body: " + body); // Debug-Ausgabe
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRevier(@PathVariable Long id) {
        Optional<Revier> revierOptional = revierRepository.findById(id);
        if (revierOptional.isPresent()) {
            Revier revier = revierOptional.get();


            revierPflegerRepository.findAllByRevier(revier).forEach(revierPfleger -> revierPflegerRepository.delete(revierPfleger));
            revierRepository.delete(revier);
            return ResponseEntity.ok("Revier erfolgreich gelöscht!");
        } else {
            return ResponseEntity.badRequest().body("Revier nicht gefunden!");
        }




    }

    @PostMapping("/removePfleger")
    public ResponseEntity<String> removePfleger(@RequestBody String body) {
        System.out.println("TTTReceived Body: " + body); // Debug-Ausgabe
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long pflegerId = jsonObject.get("pflegerId").getAsLong();

        Revier revier = revierRepository.findById(revierId).get();
        if (revier == null) {
            return ResponseEntity.badRequest().body("Revier nicht gefunden!");
        }

        Pfleger pfleger = pflegerRepository.findById(pflegerId).get();

        if (pfleger == null) {
            return ResponseEntity.badRequest().body("Pfleger nicht gefunden!");
        }

        RevierPfleger revierPfleger = revierPflegerRepository.findByRevierAndPfleger(revier, pfleger);
        if (revierPfleger == null) {
            return ResponseEntity.badRequest().body("Pfleger nicht im Revier gefunden!");
        }

        revierPflegerRepository.delete(revierPfleger);
        return ResponseEntity.ok("Pfleger erfolgreich entfernt!");

    }

    @PostMapping("/addPfleger")
    public ResponseEntity<String> addPfleger(@RequestBody String body) {
        System.out.println("TTTTRTeceived Body: " + body); // Debug-Ausgabe
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        Long revierId = jsonObject.get("revierId").getAsLong();
        Long pflegerId = jsonObject.get("pflegerId").getAsLong();

        Revier revier = revierRepository.findById(revierId).get();
        if (revier == null) {
            return ResponseEntity.badRequest().body("Revier nicht gefunden!");
        }

        Pfleger pfleger = pflegerRepository.findById(pflegerId).
                orElseThrow(() -> new IllegalArgumentException("Pfleger nicht gefunden!"));

        RevierPfleger revierPfleger = new RevierPfleger(revier, pfleger);
        revierPflegerRepository.save(revierPfleger);
        return ResponseEntity.ok("Pfleger erfolgreich hinzugefügt!");
    }

}
