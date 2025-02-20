package net.ohnonick2.naturzooprojekt.frontend.settings;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller
public class SettingsController {

    @Autowired
    private Pflegerrepository pflegerrepository;

    // GET-Anfrage, um die Einstellungen-Seite anzuzeigen
    @GetMapping("/settings")
    public String settings(Model model) {

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();

        Pfleger pfleger = pflegerrepository.findPflegerById(userloggedIn.get("id").getAsLong());

        if (pfleger.getBild() != null) {
            model.addAttribute("profilbild", pfleger.getBild());
        }



        return "settings";
    }

    // POST-Anfrage zum Verarbeiten des Formulars
    @PostMapping("/settings")
    public String settingsPost(@RequestParam("profilbild") MultipartFile profilbild, Model model) {
        if (!profilbild.isEmpty()) {
            try {
                // Hier kannst du das Bild speichern oder weiterverarbeiten, z.B. in einem Dateisystem oder einer Datenbank
                byte[] imageBytes = profilbild.getBytes();

                String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                JsonObject userloggedIn = JsonParser.parseString(username).getAsJsonObject();

                Pfleger pfleger = pflegerrepository.findPflegerById(userloggedIn.get("id").getAsLong());

                // Beispiel: Speichern in einer Datei

                pfleger.setProfilbild(imageBytes);
                pflegerrepository.save(pfleger);



                model.addAttribute("message", "Profilbild erfolgreich hochgeladen!");
            } catch (IOException e) {
                model.addAttribute("message", "Fehler beim Hochladen des Bildes!");
                e.printStackTrace();
            }
        } else {
            model.addAttribute("message", "Kein Bild ausgewählt!");
        }
        return "settings";  // Die Seite wird nach der Verarbeitung neu geladen
    }
}
