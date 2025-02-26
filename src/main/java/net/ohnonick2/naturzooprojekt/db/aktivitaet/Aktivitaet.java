package net.ohnonick2.naturzooprojekt.db.aktivitaet;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.utils.ActivityAction;

import java.time.LocalDateTime;
@Table(name = "aktivitaet")
@Entity

public class Aktivitaet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityAction action;

    private long userId;
    private String username;

    private LocalDateTime timestamp;
    private String description;



    public Aktivitaet() {
    }

    public Aktivitaet(ActivityAction action, long userId, String username,String description) {
        this.action = action;
        this.userId = userId;
        this.username = username;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }


    public ActivityAction getAction() {
        return action;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setAction(ActivityAction action) {
        this.action = action;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


}
