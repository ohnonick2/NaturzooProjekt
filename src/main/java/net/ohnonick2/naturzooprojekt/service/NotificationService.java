package net.ohnonick2.naturzooprojekt.service;

import net.ohnonick2.naturzooprojekt.db.notification.Notification;
import net.ohnonick2.naturzooprojekt.db.notification.NotificationReadId;
import net.ohnonick2.naturzooprojekt.db.notification.NotificationReadPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.NotificationReadRepository;
import net.ohnonick2.naturzooprojekt.repository.NotificationRepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationReadRepository notificationReadRepository;
    @Autowired
    private Pflegerrepository pflegerrepository;


    public NotificationService() {
    }

    public boolean createNotification(Notification notification) {
        notificationRepository.save(notification);
        return true;
    }

    public boolean resetNotificationbyPlayer(long pflegerId , long notificationId) {
        Pfleger pfleger = pflegerrepository.findPflegerById(pflegerId);
        if (pfleger == null) {
            return false;
        }
        Notification notification = notificationRepository.findNotificationById(notificationId);
        if (notification == null) {
            return false;
        }

        NotificationReadPfleger notificationRead = notificationReadRepository.findNotifificationReadPflegersByPflegerAndNotification(pfleger, notification);
        if (notificationRead == null) {
            return false;
        }

        notificationReadRepository.delete(notificationRead);


        return true;
    }

    public boolean deleteNotification(Notification notification) {
        try {
            notificationReadRepository.findNotifificationReadPflegersByNotification(notification).forEach(notificationReadRepository::delete);
            notificationRepository.delete(notification);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Notification findNotificationById(long id) {
        return notificationRepository.findNotificationById(id);
    }

    public void markNotificationAsRead(long notificationId, long pflegerId) {
        Pfleger pfleger = pflegerrepository.findPflegerById(pflegerId);
        Notification notification = notificationRepository.findNotificationById(notificationId);
        if (notification == null || pfleger == null) {
            return;
        }
        NotificationReadPfleger notificationRead = new NotificationReadPfleger(notification, pfleger);
        notificationReadRepository.save(notificationRead);
    }

    public List<Notification> getNotificationsForPfleger(long pflegerId) {
        Pfleger pfleger = pflegerrepository.findPflegerById(pflegerId);
        if (pfleger == null) {
            return null;
        }
        List<NotificationReadPfleger> notificationReads = notificationReadRepository.findNotifificationReadPflegersByPfleger(pfleger);
        List<Notification> notifications = notificationRepository.findAll();
        for (NotificationReadPfleger notificationRead : notificationReads) {
            notifications.remove(notificationRead.getNotification());
        }
        return notifications;

    }

    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Scheduled(fixedRate = 10000)
    public void deleteOldNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        long currentTime = System.currentTimeMillis();
        for (Notification notification : notifications) {
            if (notification.getDeleteon() < currentTime) {

                System.out.println("Deleting notification: " + notification.getId() + " - " + notification.getTitle() + " - " + notification.getMessage()); ;

                notificationReadRepository.findNotifificationReadPflegersByNotification(notification).forEach(notificationReadRepository::delete);
                notificationRepository.delete(notification);

            }
        }
    }

    public List<NotificationReadPfleger> findAll() {
        return notificationReadRepository.findAll();
    }



}
