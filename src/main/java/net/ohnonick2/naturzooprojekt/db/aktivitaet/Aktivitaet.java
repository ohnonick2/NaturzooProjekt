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


    private String pfleger;

    private LocalDateTime timestamp;

    @Column(length = 500) // Limitiert die Länge der Details, um Datenbanküberlauf zu vermeiden
    private String details;



    // Konstruktoren

    public Aktivitaet() {
    }

    public Aktivitaet(ActivityAction action, Pfleger pfleger, String details) {
        this.action = action;
        this.pfleger = pfleger.getBenutzername();
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }



    @PrePersist
    public void onPrePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }


    // Getter und Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActivityAction getAction() {
        return action;
    }

    public void setAction(ActivityAction action) {
        this.action = action;
    }




    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
