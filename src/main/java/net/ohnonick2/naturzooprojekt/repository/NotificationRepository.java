package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.notification.Notification;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findNotificationById(Long id);



}
