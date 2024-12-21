package net.ohnonick2.naturzooprojekt.db.gebaeude;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;

import java.time.LocalDate;

@Entity
@IdClass(GebaeudeTierId.class)
@Table(name = "gebaeude_tier")
public class GebaeudeTier {


    @Id
    @ManyToOne
    @JoinColumn(name = "gebaeude_id")
    private Gebaeude gebaeudeId;

    @Id
    @ManyToOne
    @JoinColumn(name = "tier_id")
    private Tier tierId;


    public GebaeudeTier() {
    }

    public GebaeudeTier(Gebaeude gebaeude, Tier tier) {
        this.gebaeudeId = gebaeude;
        this.tierId = tier;
    }

    public Gebaeude getGebaeude() {
        return gebaeudeId;
    }

    public void setGebaeude(Gebaeude gebaeude) {
        this.gebaeudeId = gebaeude;
    }

    public Gebaeude getGebaeudeId() {
        return gebaeudeId;
    }

    public void setGebaeudeId(Gebaeude gebaeude) {
        this.gebaeudeId = gebaeude;
    }
}