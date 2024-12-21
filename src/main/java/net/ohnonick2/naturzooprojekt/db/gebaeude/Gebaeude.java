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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
