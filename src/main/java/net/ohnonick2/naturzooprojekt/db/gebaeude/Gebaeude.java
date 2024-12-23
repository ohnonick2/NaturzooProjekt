package net.ohnonick2.naturzooprojekt.db.gebaeude;

import jakarta.persistence.*;

@Entity
@Table(name = "gebaeude")
public class Gebaeude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(name = "Beschreibung", nullable = true, length = 256)
    private String beschreibung;

    @Column(name = "MaximaleKapazitaet", nullable = true)
    private int maximaleKapazitaet;

    public Gebaeude() {
    }

    public Gebaeude(String name, String beschreibung, int maximaleKapazitaet) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.maximaleKapazitaet = maximaleKapazitaet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getMaximaleKapazitaet() {
        return maximaleKapazitaet;
    }

    public void setMaximaleKapazitaet(int maximaleKapazitaet) {
        this.maximaleKapazitaet = maximaleKapazitaet;
    }
}
