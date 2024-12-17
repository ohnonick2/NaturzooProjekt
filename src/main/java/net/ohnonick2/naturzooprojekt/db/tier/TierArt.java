package net.ohnonick2.naturzooprojekt.db.tier;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;

@Entity(name = "tierart")
public class TierArt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public TierArt() {
    }

    public TierArt(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



}
