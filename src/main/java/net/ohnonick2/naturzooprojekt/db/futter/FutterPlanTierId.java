package net.ohnonick2.naturzooprojekt.db.futter;

import java.io.Serializable;
import java.util.Objects;

public class FutterPlanTierId implements Serializable {

    private Long futterplan; // Name muss mit dem Feldnamen in FutterPlanTier übereinstimmen
    private Long tier;       // Name muss mit dem Feldnamen in FutterPlanTier übereinstimmen

    public FutterPlanTierId() {
    }

    public FutterPlanTierId(Long futterplan, Long tier) {
        this.futterplan = futterplan;
        this.tier = tier;
    }

    // Getter und Setter
    public Long getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(Long futterplan) {
        this.futterplan = futterplan;
    }

    public Long getTier() {
        return tier;
    }

    public void setTier(Long tier) {
        this.tier = tier;
    }

    // equals und hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterPlanTierId that = (FutterPlanTierId) o;
        return Objects.equals(futterplan, that.futterplan) && Objects.equals(tier, that.tier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterplan, tier);
    }
}
