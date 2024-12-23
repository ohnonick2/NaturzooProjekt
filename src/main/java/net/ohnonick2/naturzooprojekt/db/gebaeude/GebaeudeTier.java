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
    @JoinColumn(name = "gebaeude_id", nullable = false)
    private Gebaeude gebaeude;

    @Id
    @ManyToOne
    @JoinColumn(name = "tier_id", nullable = false)
    private Tier tier;

    public GebaeudeTier() {
    }

    public GebaeudeTier(Gebaeude gebaeude, Tier tier) {
        this.gebaeude = gebaeude;
        this.tier = tier;
    }

    public Gebaeude getGebaeude() {
        return gebaeude;
    }

    public void setGebaeude(Gebaeude gebaeude) {
        this.gebaeude = gebaeude;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }
}
