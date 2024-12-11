package net.ohnonick2.naturzooprojekt.db.wochentag;

import jakarta.persistence.*;

@Entity
public class Wochentag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;


    public Wochentag() {
    }

    public Wochentag(String name) {
        this.name = name;
    }



}
