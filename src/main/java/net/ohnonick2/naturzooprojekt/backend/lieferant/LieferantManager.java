package net.ohnonick2.naturzooprojekt.backend.lieferant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;
import net.ohnonick2.naturzooprojekt.repository.AdresseRepository;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lieferant")
public class LieferantManager {

    @Autowired
    private LieferantRepository lieferantRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private Ortrepository ortrepository;


    @PostMapping("/add")
    public ResponseEntity<String> addLieferant(@RequestBody String body) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        if (json == null) {
            return ResponseEntity.badRequest().body("Invalid JSON");
        }


        

        Lieferant lieferant = new Lieferant("name", null, "telefonnummer", "ansprechpartner");






        return ResponseEntity.ok("Lieferant added");
    }



}
