package net.ohnonick2.naturzooprojekt.db.futter;


import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;

@Entity(name = "futter")
public class Futter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    //menge in gramm
    private double menge;

    @OneToOne
    @JoinColumn(name = "lieferant")
    private Lieferant lieferant;

    public Futter() {
    }

    public Futter(String name, double menge , Lieferant lieferant) {
        this.name = name;
        this.menge = menge;
        this.lieferant = lieferant;
    }



}
