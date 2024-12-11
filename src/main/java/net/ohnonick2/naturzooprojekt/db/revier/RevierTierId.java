package net.ohnonick2.naturzooprojekt.db.revier;

import java.io.Serializable;
import java.util.Objects;

public class RevierTierId implements Serializable {

    private Long revierId;
    private Long tierId;

    public RevierTierId() {
    }

    public RevierTierId(Long revierId, Long tierId) {
        this.revierId = revierId;
        this.tierId = tierId;
    }

    public Long getRevierId() {
        return revierId;
    }

    public void setRevierId(Long revierId) {
        this.revierId = revierId;
    }

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevierTierId that = (RevierTierId) o;
        return Objects.equals(revierId, that.revierId) && Objects.equals(tierId, that.tierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(revierId, tierId);
    }
}
