package net.ohnonick2.naturzooprojekt.backend.notification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.notification.Notification;
import net.ohnonick2.naturzooprojekt.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationManager {

    @Autowired
    private NotificationService notificationService;



    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        if (!jsonObject.has("title") || !jsonObject.has("message") || !jsonObject.has("deleteon")) {
            return ResponseEntity.badRequest().body("Missing parameters");
        }

        String title = jsonObject.get("title").getAsString();
        String message = jsonObject.get("message").getAsString();
        long deleteon = jsonObject.get("deleteon").getAsLong();

        Notification notification = new Notification(title, message, deleteon);
        notificationService.createNotification(notification);



        return ResponseEntity.ok("Notification created");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteNotification(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        System.out.println(body);
        if (!jsonObject.has("id")) {
            return ResponseEntity.badRequest().body("Missing parameters");
        }

        long id = jsonObject.get("id").getAsLong();


        boolean notification = notificationService.deleteNotification(notificationService.findNotificationById(id));

        if (!notification) {
            return ResponseEntity.badRequest().body("Notification not found");
        }

        return ResponseEntity.ok("Notification deleted");

    }

    @PostMapping("/markasread")
    public ResponseEntity<String> markNotificationAsRead(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        System.out.println(body);
        if (!jsonObject.has("notificationId") || !jsonObject.has("pflegerId")) {
            return ResponseEntity.badRequest().body("Missing parameters");
        }

        long notificationId = jsonObject.get("notificationId").getAsLong();
        long pflegerId = jsonObject.get("pflegerId").getAsLong();

        notificationService.markNotificationAsRead(notificationId, pflegerId);

        return ResponseEntity.ok("Notification marked as read");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editNotification(@RequestBody String body) {
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        System.out.println(body);
        if (!jsonObject.has("id") || !jsonObject.has("title") || !jsonObject.has("message") || !jsonObject.has("deleteon")) {
            return ResponseEntity.badRequest().body("Missing parameters");
        }

        long id = jsonObject.get("id").getAsLong();
        String title = jsonObject.get("title").getAsString();
        String message = jsonObject.get("message").getAsString();
        long deleteon = jsonObject.get("deleteon").getAsLong();

        Notification notification = notificationService.findNotificationById(id);
        if (notification == null) {
            return ResponseEntity.badRequest().body("Notification not found");
        }

        notification.setTitle(title);
        notification.setMessage(message);
        notification.setDeleteon(deleteon);

        notificationService.createNotification(notification);

        return ResponseEntity.ok("Notification edited");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetNotification(@RequestBody String body) {
        System.out.println(body);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
        if (!jsonObject.has("players") && !jsonObject.has("notificationId")) {

            return ResponseEntity.badRequest().body("Missing parameters");
        }

        JsonArray players = jsonObject.get("players").getAsJsonArray();
        if (players == null) {
            return ResponseEntity.badRequest().body("No players found");
        }

        if (players.isEmpty()) {
            return ResponseEntity.badRequest().body("No players found");
        }

        if (jsonObject.get("notificationId") == null) {
            return ResponseEntity.badRequest().body("No notificationid found");
        }


        long notificationid = jsonObject.get("notificationId").getAsLong();

        for (int i = 0; i < players.size(); i++) {
            long pflegerid = players.get(i).getAsLong();
            if (!notificationService.resetNotificationbyPlayer(pflegerid, notificationid)) {
                return ResponseEntity.badRequest().body("Error while reseting notification");
            }
        }

        return ResponseEntity.ok("Notification reset");
    }



}
