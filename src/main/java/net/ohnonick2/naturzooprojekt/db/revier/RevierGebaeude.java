package net.ohnonick2.naturzooprojekt.db.revier;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import java.io.Serializable;

@Entity
@Table(name = "revier_gebaeude")
@IdClass(RevierGebaeudeId.class)
public class RevierGebaeude implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "revier_id", nullable = false)
    private Revier revier;

    @Id
    @ManyToOne
    @JoinColumn(name = "gebaeude_id", nullable = false)
    private Gebaeude gebaeude;

    public RevierGebaeude() {}

    public RevierGebaeude(Revier revier, Gebaeude gebaeude) {
        this.revier = revier;
        this.gebaeude = gebaeude;
    }

    public Revier getRevier() {
        return revier;
    }

    public void setRevier(Revier revier) {
        this.revier = revier;
    }

    public Gebaeude getGebaeude() {
        return gebaeude;
    }

    public void setGebaeude(Gebaeude gebaeude) {
        this.gebaeude = gebaeude;
    }
}
