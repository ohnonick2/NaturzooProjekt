package net.ohnonick2.naturzooprojekt.db.futter;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity(name = "futterplan")
public class FutterPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Strategie anpassen, z.B., SEQUENCE, TABLE
    private Long id;

    private Date futterTag;
    private int menge;


    public FutterPlan() {
    }

    public FutterPlan(Date futterTag, int menge) {

        this.menge = menge;
        this.futterTag = futterTag;




    }






    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
