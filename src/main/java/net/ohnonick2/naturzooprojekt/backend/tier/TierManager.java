package net.ohnonick2.naturzooprojekt.backend.tier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.Nullable;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanTier;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping(value = "/api/tier")
public class TierManager {

    @Autowired
    private Tierrespository tierrespository;

    @Autowired
    private Tierartrepository tierartrepository;

    @Autowired
    private FutterPlanTierRepositority futterPlanTierRepositority;

    @Autowired
    private FutterPlanRepository futterPlanRepository;

    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private RevierTierRespository revierTierRespository;

    @PostMapping("/add")
    public ResponseEntity<String> addTier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().build();
        }
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        LocalDate geburtsdatum = LocalDate.parse(jsonObject.get("geburtsdatum").getAsString());
        LocalDate sterbedatum = jsonObject.get("sterbedatum") == null ? null : LocalDate.parse(jsonObject.get("sterbedatum").getAsString());
        TierGeschlecht geschlecht = TierGeschlecht.valueOf(jsonObject.get("geschlecht").getAsString());
        TierArt tierArt = tierartrepository.findByName(jsonObject.get("tierArt").getAsString());
        String revier = jsonObject.get("revier").getAsString();
        if (name == null || geburtsdatum == null || geschlecht == null || tierArt == null || revier == null) {
            return ResponseEntity.badRequest().build();
        }
        Tier tier = new Tier(name, geburtsdatum,   sterbedatum , null, geschlecht, tierArt);

        Revier revier1 = revierRepository.findRevierByName(revier);
        if (revier1 == null) {
            return ResponseEntity.badRequest().build();
        }

        revierTierRespository.save(new RevierTier(revier1, tier));



        tierrespository.save(tier);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteTier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().build();
        }
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        long id = jsonObject.get("id").getAsLong();
        if (id == 0) {
            return ResponseEntity.badRequest().build();
        }
        tierrespository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<String> updateTier(@RequestBody String body) {
        if (body == null || body.isEmpty() || !body.startsWith("{") || !body.endsWith("}")) {
            return ResponseEntity.badRequest().build();
        }
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        LocalDate geburtsdatum = LocalDate.parse(jsonObject.get("geburtsdatum").getAsString());
        LocalDate sterbedatum = jsonObject.get("sterbedatum") == null ? null : LocalDate.parse(jsonObject.get("sterbedatum").getAsString());
        TierGeschlecht geschlecht = TierGeschlecht.valueOf(jsonObject.get("geschlecht").getAsString());
        TierArt tierArt = tierartrepository.findById(jsonObject.get("tierArt").getAsLong()).get();
        if (id == 0 || name == null || geburtsdatum == null || geschlecht == null || tierArt == null) {
            return ResponseEntity.badRequest().build();
        }
        Tier tier = tierrespository.findById(id).get();
        tier.setName(name);
        tier.setGeburtsdatum(geburtsdatum);
        tier.setSterbedatum(sterbedatum);
        tier.setGeschlecht(geschlecht);
        tier.setTierArt(tierArt);
        tierrespository.save(tier);
        return ResponseEntity.ok().build();
    }


}
