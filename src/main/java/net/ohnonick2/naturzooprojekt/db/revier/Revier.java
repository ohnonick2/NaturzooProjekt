package net.ohnonick2.naturzooprojekt.db.revier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "revier")
public class Revier {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    public Revier(String name) {
        this.name = name;
    }


    public Revier() {

    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
