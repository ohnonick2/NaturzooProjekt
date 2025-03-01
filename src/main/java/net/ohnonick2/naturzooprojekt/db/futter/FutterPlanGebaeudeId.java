package net.ohnonick2.naturzooprojekt.db.futter;

import java.io.Serializable;
import java.util.Objects;

public class FutterPlanGebaeudeId implements Serializable {
    private Long futterPlanId;
    private Long gebaeudeId;

    // 🛠 Standard-Konstruktoren
    public FutterPlanGebaeudeId() {}

    public FutterPlanGebaeudeId(Long futterPlanId, Long gebaeudeId) {
        this.futterPlanId = futterPlanId;
        this.gebaeudeId = gebaeudeId;
    }

    // 🛠 Getter & Setter
    public Long getFutterPlanId() {
        return futterPlanId;
    }

    public void setFutterPlanId(Long futterPlanId) {
        this.futterPlanId = futterPlanId;
    }

    public Long getGebaeudeId() {
        return gebaeudeId;
    }

    public void setGebaeudeId(Long gebaeudeId) {
        this.gebaeudeId = gebaeudeId;
    }

    // 🛠 equals & hashCode (wichtig für zusammengesetzte Schlüssel)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterPlanGebaeudeId that = (FutterPlanGebaeudeId) o;
        return Objects.equals(futterPlanId, that.futterPlanId) &&
                Objects.equals(gebaeudeId, that.gebaeudeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterPlanId, gebaeudeId);
    }
}
