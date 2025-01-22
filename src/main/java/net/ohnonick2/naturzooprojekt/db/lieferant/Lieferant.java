package net.ohnonick2.naturzooprojekt.db.lieferant;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.adresse.Adresse;

@Entity(name = "lieferant")
public class Lieferant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "adresse")
    private Adresse adresse;
    private String telefonnummer;
    private String ansprechpartner;

    public Lieferant() {
    }

    public Lieferant(String name, Adresse adresse, String telefonnummer, String ansprechpartner) {
        this.name = name;
        this.adresse = adresse;
        this.telefonnummer = telefonnummer;
        this.ansprechpartner = ansprechpartner;
    }

    public String getName() {
        return name;
    }



    public String getTelefonnummer() {
        return telefonnummer;
    }


    public String getAnsprechpartner() {
        return ansprechpartner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }



    public void setAnsprechpartner(String ansprechpartner) {
        this.ansprechpartner = ansprechpartner;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Long getId() {
        return id;
    }


}
