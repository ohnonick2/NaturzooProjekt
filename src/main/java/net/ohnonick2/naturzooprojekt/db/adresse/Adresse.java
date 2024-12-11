package net.ohnonick2.naturzooprojekt.db.adresse;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;

@Entity
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String strasse;
    private String hausnummer;

    @OneToOne
    @JoinColumn(name = "ort")
    private Ort ort;

    public Adresse() {
    }

    public Adresse(String strasse, String hausnummer, Ort ort) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.ort = ort;
    }


}
