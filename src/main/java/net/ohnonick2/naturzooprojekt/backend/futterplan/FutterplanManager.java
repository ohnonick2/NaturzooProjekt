package net.ohnonick2.naturzooprojekt.backend.futterplan;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.FutterPlanRepository;
import net.ohnonick2.naturzooprojekt.repository.FutterPlanWochentagRepository;
import net.ohnonick2.naturzooprojekt.repository.FutterRepository;
import net.ohnonick2.naturzooprojekt.repository.FutterplanFutterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping(value = "/add")
    public ResponseEntity<String> addFutterplan(@RequestBody String body) {
        // Parse the JSON body
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        // Validate the presence of the "name" field in the incoming JSON
        if (json == null || !json.has("name")) {
            return ResponseEntity.badRequest().body("Invalid JSON or missing 'name' field.");
        }

        // Extract the "name" from the JSON object
        String futterplanName = json.get("name").getAsString();

        // Create a new FutterPlan object
        FutterPlan futterPlan = new FutterPlan(futterplanName);

        // Save the FutterPlan to the repository
        futterPlanRepository.save(futterPlan);

        // Return a success message
        return ResponseEntity.ok("Futterplan added successfully.");
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<String> deleteFutterplan(@RequestBody String body) {

        System.out.println(body);
        return ResponseEntity.ok("Futterplan deleted successfully.");
    }




}
