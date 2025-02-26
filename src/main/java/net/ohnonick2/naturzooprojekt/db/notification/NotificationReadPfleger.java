package net.ohnonick2.naturzooprojekt.db.notification;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import java.io.Serializable;

@Entity
@Table(name = "notification_read_pfleger")
@IdClass(NotificationReadId.class)
public class NotificationReadPfleger  {

    @Id
    @ManyToOne()
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Id
    @ManyToOne()
    @JoinColumn(name = "pfleger_id", nullable = false)
    private Pfleger pfleger;

    private long readOn;


    public NotificationReadPfleger(Notification notification, Pfleger pfleger) {
        this.notification = notification;
        this.pfleger = pfleger;
        this.readOn = System.currentTimeMillis();
    }


    public NotificationReadPfleger() {
    }

    // 🔹 Getter & Setter
    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Pfleger getPfleger() {
        return pfleger;
    }

    public void setPfleger(Pfleger pfleger) {
        this.pfleger = pfleger;
    }

    public long getReadOn() {
        return readOn;
    }

    public void setReadOn(long readOn) {
        this.readOn = readOn;
    }
}
