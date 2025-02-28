package net.ohnonick2.naturzooprojekt.db.notification;

import java.io.Serializable;
import java.util.Objects;

public class NotificationReadId implements Serializable {

    private Long notification;
    private Long pfleger;

    public NotificationReadId() {}

    public NotificationReadId(Long notification, Long pflegerId) {
        this.notification = notification;
        this.pfleger = pflegerId;
    }

    public Long getNotification() {
        return notification;
    }

    public void setNotification(Long notification) {
        this.notification = notification;
    }

    public Long getPfleger() {
        return pfleger;
    }

    public void setPfleger(Long pfleger) {
        this.pfleger = pfleger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationReadId that = (NotificationReadId) o;
        return Objects.equals(notification, that.notification) &&
                Objects.equals(pfleger, that.pfleger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notification, pfleger);
    }
}
