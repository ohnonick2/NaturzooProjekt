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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public Ort getOrt() {
        return ort;
    }
}
