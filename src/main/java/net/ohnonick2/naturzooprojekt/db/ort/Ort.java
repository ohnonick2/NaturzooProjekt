package net.ohnonick2.naturzooprojekt.db.ort;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Ort {

    @Id
    private Integer plz;

    private String ortname;

    public Ort() {
    }

    public Ort(Integer plz, String ortname) {
        this.plz = plz;
        this.ortname = ortname;
    }

    public Integer getPlz() {
        return plz;
    }

    public String getOrtname() {
        return ortname;
    }

    public void setOrtname(String ortname) {
        this.ortname = ortname;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }
}
