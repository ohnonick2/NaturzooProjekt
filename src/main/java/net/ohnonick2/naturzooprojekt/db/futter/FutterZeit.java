package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * FutterZeit Klasse die die Fütterungszeiten speichert und verwaltet
 */



@Entity(name = "futterzeit")
public class FutterZeit {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String uhrzeit;

    public FutterZeit() {
    }

    public FutterZeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

}
