package net.ohnonick2.naturzooprojekt.db.gebaeude;

import java.io.Serializable;
import java.util.Objects;

public class GebaeudeTierId implements Serializable {

    private long gebaeudeId;
    private long tierId;

    public GebaeudeTierId() {
    }

    public GebaeudeTierId(long gebaeudeId, long tierId) {
        this.gebaeudeId = gebaeudeId;
        this.tierId = tierId;
    }

    public long getGebaeudeId() {
        return gebaeudeId;
    }

    public void setGebaeudeId(long gebaeudeId) {
        this.gebaeudeId = gebaeudeId;
    }

    public long getTierId() {
        return tierId;
    }

    public void setTierId(long tierId) {
        this.tierId = tierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GebaeudeTierId that = (GebaeudeTierId) o;
        return gebaeudeId == that.gebaeudeId && tierId == that.tierId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gebaeudeId, tierId);
    }
}
