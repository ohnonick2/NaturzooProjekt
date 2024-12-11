package net.ohnonick2.naturzooprojekt.db.revier;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;

import java.io.Serializable;

@Entity(name = "revier_tier")
@IdClass(RevierTierId.class)
public class RevierTier implements Serializable {

    @Id
    @JoinColumn(name = "revier_id")
    @OneToOne
    private Revier revierId;

    @Id
    @JoinColumn(name = "tier_id")
    @OneToOne
    private Tier tierId;

    public RevierTier() {
    }

    public RevierTier(Revier revierId, Tier tierId) {
        this.revierId = revierId;
        this.tierId = tierId;
    }

    public Revier getRevierId() {
        return revierId;
    }

    public Tier getTierId() {
        return tierId;
    }
}
