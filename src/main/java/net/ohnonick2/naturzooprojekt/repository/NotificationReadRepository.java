package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.notification.Notification;

import net.ohnonick2.naturzooprojekt.db.notification.NotificationReadPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationReadRepository extends JpaRepository<NotificationReadPfleger, Long> {


    List<NotificationReadPfleger> findNotifificationReadPflegersByPfleger(Pfleger pfleger);
    List<NotificationReadPfleger> findNotifificationReadPflegersByNotification(Notification notification);

    NotificationReadPfleger findNotifificationReadPflegersByPflegerAndNotification(Pfleger pfleger, Notification notification);



}
