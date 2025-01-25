package net.ohnonick2.naturzooprojekt.db.revier;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPflegerId;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;

@Entity
@Table(name = "revier_pfleger")
@IdClass(RevierPflegerId.class)
public class RevierPfleger {

    @Id
    @ManyToOne
    @JoinColumn(name = "revier_id", nullable = false)
    private Revier revier;

    @Id
    @ManyToOne
    @JoinColumn(name = "pfleger_id", nullable = false)
    private Pfleger pfleger;

    public RevierPfleger() {
    }

    public RevierPfleger(Revier revier, Pfleger pfleger) {
        this.revier = revier;
        this.pfleger = pfleger;
    }

    public Revier getRevier() {
        return revier;
    }

    public void setRevier(Revier revier) {
        this.revier = revier;
    }

    public Pfleger getPfleger() {
        return pfleger;
    }

    public void setPfleger(Pfleger pfleger) {
        this.pfleger = pfleger;
    }


}
