package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;

@Entity(name = "futter")
public class Futter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Menge in Gramm
    @Column(nullable = false)
    private double menge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lieferant_id", nullable = false)
    private Lieferant lieferant;

    // Konstruktoren
    public Futter() {
    }

    public Futter(String name, double menge, Lieferant lieferant) {
        this.name = name;
        this.menge = menge;
        this.lieferant = lieferant;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMenge() {
        return menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public Lieferant getLieferant() {
        return lieferant;
    }

    public void setLieferant(Lieferant lieferant) {
        this.lieferant = lieferant;
    }

}
