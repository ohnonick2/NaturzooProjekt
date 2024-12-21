package net.ohnonick2.naturzooprojekt.db.aktivitaet;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "aktivitaet")
@Entity
public class Aktivitaet {


    @Id
    private Long id;

    


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
