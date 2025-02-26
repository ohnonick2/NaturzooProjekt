package net.ohnonick2.naturzooprojekt.db.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;

@Entity(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String message;

    private long created;
    private long deleteon;

    public Notification() {

    }

    public Notification(String title, String message , long deleteon) {
        this.title = title;
        this.message = message;

        this.created = System.currentTimeMillis();
        this.deleteon = deleteon;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getDeleteon() {
        return deleteon;
    }

    public void setDeleteon(long deleteon) {
        this.deleteon = deleteon;
    }
}
