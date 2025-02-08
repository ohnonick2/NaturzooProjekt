package net.ohnonick2.naturzooprojekt.backend.ort;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/ort")
public class OrtManager {  // Corrected the class name spelling

    @Autowired
    private Ortrepository ortrepository;

    @PostMapping("/add")
    public ResponseEntity<String> createOrt(@RequestBody String body) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("name") || !json.has("plz")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (ortrepository.findByPlz(json.get("plz").getAsInt()) != null) {
            return ResponseEntity.badRequest().body("Ort schon vorhanden");
        }

        Ort ort = new Ort(json.get("plz").getAsInt(), json.get("name").getAsString());
        ortrepository.save(ort);
        return ResponseEntity.ok("Ort added");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteOrt(@RequestBody String body) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("plz")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Ort ort = ortrepository.findByPlz(json.get("plz").getAsInt());
        if (ort == null) {
            return ResponseEntity.badRequest().body("Ort nicht vorhanden");
        }
        ortrepository.delete(ort);
        return ResponseEntity.ok("Ort deleted");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editOrt(@RequestBody String body) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        if (!json.has("name") || !json.has("plz")) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }

        Ort ort = ortrepository.findByPlz(json.get("plz").getAsInt());
        if (ort == null) {
            return ResponseEntity.badRequest().body("Ort nicht vorhanden");
        }

        ort.setOrtname(json.get("name").getAsString());
        ortrepository.save(ort);
        return ResponseEntity.ok("Ort edited");
    }


}
