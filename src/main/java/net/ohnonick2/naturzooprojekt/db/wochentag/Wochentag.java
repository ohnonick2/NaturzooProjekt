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



}
