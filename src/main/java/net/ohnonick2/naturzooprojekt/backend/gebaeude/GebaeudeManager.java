package net.ohnonick2.naturzooprojekt.backend.gebaeude;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.aktivitaet.Aktivitaet;
import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.AktivitaetRepository;
import net.ohnonick2.naturzooprojekt.repository.GebaeudeRepository;
import net.ohnonick2.naturzooprojekt.repository.GebaeudeTierRepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.service.AktivitaetService;
import net.ohnonick2.naturzooprojekt.utils.ActivityAction;
import net.ohnonick2.naturzooprojekt.utils.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/gebaeude")
public class GebaeudeManager {

    @Autowired
    private GebaeudeRepository gebaeudeRepository;

    @Autowired
    private GebaeudeTierRepository gebaeudeTierRepository;


    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private AktivitaetService aktivitaetService;

    @Autowired
    private AktivitaetRepository aktivitaetRepository;



    @PostMapping("/delete")
    public ResponseEntity<String> deleteGebaeude(@RequestBody String body) {


        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();

        Pfleger pfleger = pflegerrepository.findPflegerById(userloggedIn.get("id").getAsLong());


        aktivitaetRepository.save(new Aktivitaet(ActivityAction.GEBÄUDE_DELETE, pfleger, "Gebäude gelöscht"));





        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json == null) {
            System.out.println("json is null");
            return ResponseEntity.badRequest().build();
        }

        System.out.println(json.get("id").getAsString());

        if (gebaeudeRepository.findById(Long.parseLong(json.get("id").getAsString())) == null) {
            return ResponseEntity.badRequest().build();
        }

        //check ob gebaeude noch tiere hat und wemm ja return badrequest mit meldung
        if (gebaeudeTierRepository.findByGebaeude(gebaeudeRepository.findById(Long.parseLong(json.get("id").getAsString()))).size() > 0) {
            return ResponseEntity.badRequest().build();
        }

        gebaeudeRepository.delete(gebaeudeRepository.findById(Long.parseLong(json.get("id").getAsString())));
        return ResponseEntity.ok().build();

    }

    @PostMapping("/add")
    public ResponseEntity<String> addGebaeude(@RequestBody String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json == null) {
            return ResponseEntity.badRequest().build();
        }
        if (json.get("beschreibung").getAsString().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        if (gebaeudeRepository.findByName(json.get("name").getAsString()) != null) {
            return ResponseEntity.badRequest().build();
        }

        gebaeudeRepository.save(new Gebaeude(json.get("name").getAsString() , json.get("beschreibung").getAsString() , json.get("maximaleKapazitaet").getAsInt()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editGebaeude(@RequestBody String body) {

        if (JsonParser.parseString(body).getAsJsonObject() == null) {
            return ResponseEntity.badRequest().build();
        }

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json.get("beschreibung").getAsString().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        if (gebaeudeRepository.findById(json.get("id").getAsLong()) == null) {
            return ResponseEntity.badRequest().build();
        }



        Gebaeude gebaeude = gebaeudeRepository.findById(json.get("id").getAsLong());
        gebaeude.setName(json.get("name").getAsString());
        gebaeude.setBeschreibung(json.get("beschreibung").getAsString());
        gebaeude.setMaximaleKapazitaet(json.get("maximaleKapazitaet").getAsInt());
        gebaeudeRepository.save(gebaeude);


        return ResponseEntity.ok("Gebäude erfolgreich aktualisiert.");
    }


}
