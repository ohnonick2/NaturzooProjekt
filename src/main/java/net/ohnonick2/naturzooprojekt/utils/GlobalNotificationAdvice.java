package net.ohnonick2.naturzooprojekt.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.notification.Notification;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalNotificationAdvice {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Pflegerrepository pflegerrepository;

    @ModelAttribute("notifications")
    public List<Notification> addNotificationsToModel(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Falls keine Authentifizierung vorhanden ist, gibt es keine Benachrichtigungen
            if (authentication == null || !authentication.isAuthenticated()) {
                return Collections.emptyList();
            }

            // Principal als UserDetails casten
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof UserDetails)) {
                return Collections.emptyList();
            }

            String jsonUserDetails = ((UserDetails) principal).getUsername();

            // JSON parsen
            JsonObject jsonObject = JsonParser.parseString(jsonUserDetails).getAsJsonObject();
            Long pflegerId = jsonObject.has("id") ? jsonObject.get("id").getAsLong() : null;

            if (pflegerId == null) {
                return Collections.emptyList();
            }

            // Pfleger abrufen
            Optional<Pfleger> optionalPfleger = Optional.ofNullable(pflegerrepository.findPflegerById(pflegerId));
            if (optionalPfleger.isEmpty()) {
                return Collections.emptyList();
            }
            model.addAttribute("id" , pflegerId);

            model.addAttribute("notificationsr" , notificationService.getNotificationsForPfleger(pflegerId));

            // Benachrichtigungen für den Pfleger abrufen
            return notificationService.getNotificationsForPfleger(pflegerId);

        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Benachrichtigungen: " + e.getMessage());
            return Collections.emptyList(); // Falls ein Fehler auftritt, wird eine leere Liste zurückgegeben
        }
    }
}
