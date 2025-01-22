package net.ohnonick2.naturzooprojekt.backend.revier;

import net.ohnonick2.naturzooprojekt.db.revier.Revier;

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

    @PostMapping("/add")
    public ResponseEntity<String> addRevier(@RequestBody Revier newRevier) {
        try {
            revierRepository.save(newRevier);
            return ResponseEntity.ok("Revier erfolgreich hinzugefügt!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Hinzufügen des Reviers: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRevier(@PathVariable Long id, @RequestBody Revier updatedRevier) {
        Optional<Revier> optionalRevier = revierRepository.findById(id);

        if (optionalRevier.isPresent()) {
            Revier existingRevier = optionalRevier.get();
            existingRevier.setName(updatedRevier.getName()); // Setzt nur das Attribut `name`
            revierRepository.save(existingRevier);
            return ResponseEntity.ok("Revier erfolgreich aktualisiert!");
        } else {
            return ResponseEntity.badRequest().body("Revier mit ID " + id + " nicht gefunden.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRevier(@PathVariable Long id) {
        try {
            revierRepository.deleteById(id);
            return ResponseEntity.ok("Revier erfolgreich gelöscht!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fehler beim Löschen des Reviers: " + e.getMessage());
        }
    }


}
