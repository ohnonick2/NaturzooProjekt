package net.ohnonick2.naturzooprojekt.frontend.settings;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

    @Controller
    public class SettingsController {

        @Autowired
        private Pflegerrepository pflegerrepository;

        @GetMapping("/settings")
        public String settings(Model model) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

            JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();



            // Hole den Benutzer anhand des Benutzernamens
            Pfleger pfleger = pflegerrepository.findPflegerById(userloggedIn.get("id").getAsLong());

            if (pfleger != null && pfleger.getBild() != null) {
                model.addAttribute("profilbild", Base64.getEncoder().encodeToString(pfleger.getBild()));
            } else {
                model.addAttribute("profilbild", null);  // Optional: Platzhalter für fehlendes Bild
            }

            model.addAttribute("email", pfleger != null ? pfleger.getBenutzername() : "");  // Beispiel: E-Mail hinzufügen
            return "settings";
        }


    }
