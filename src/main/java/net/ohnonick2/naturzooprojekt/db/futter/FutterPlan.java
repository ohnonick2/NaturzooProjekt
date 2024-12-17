package net.ohnonick2.naturzooprojekt.db.futter;


import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "futterplan")
public class FutterPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Strategie anpassen, z.B., SEQUENCE, TABLE
    private Long id;

    @Column(name = "name" , nullable = false , unique = true , length = 50)
    private String name;


    public FutterPlan() {
    }

    public FutterPlan(String name) {
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
