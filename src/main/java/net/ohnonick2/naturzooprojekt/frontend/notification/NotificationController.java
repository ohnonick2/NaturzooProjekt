package net.ohnonick2.naturzooprojekt.frontend.notification;

import net.ohnonick2.naturzooprojekt.db.notification.Notification;
import net.ohnonick2.naturzooprojekt.db.notification.NotificationReadPfleger;
import net.ohnonick2.naturzooprojekt.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationReadPflegerService;

    @GetMapping("/notifications")
    public String getNotifications(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        List<Notification> allNotifications = notificationReadPflegerService.getNotifications();
        int start = Math.min(page * size, allNotifications.size());
        int end = Math.min((page + 1) * size, allNotifications.size());
        List<Notification> notificationPage = allNotifications.subList(start, end);

        model.addAttribute("notifications", notificationPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) allNotifications.size() / size));

        List<NotificationReadPfleger> notificationReads = notificationReadPflegerService.findAll();
        model.addAttribute("notificationReads", notificationReads);

        return "autharea/notification/notificationmanagement";
    }
}