package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;

@Entity(name = "tier_futterplan")
@IdClass(FutterPlanTierId.class)
public class FutterPlanTier {

    @Id
    @ManyToOne
    @JoinColumn(name = "futterplan_id")
    private FutterPlan futterplan;

    @Id
    @ManyToOne
    @JoinColumn(name = "tier_id")
    private Tier tier;


    public FutterPlanTier() {
    }

    public FutterPlanTier(FutterPlan futterplan, Tier tier) {
        this.futterplan = futterplan;
        this.tier = tier;
    }

    // Getter und Setter
    public FutterPlan getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(FutterPlan futterplan) {
        this.futterplan = futterplan;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

}
