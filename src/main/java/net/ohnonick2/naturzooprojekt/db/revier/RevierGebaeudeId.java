package net.ohnonick2.naturzooprojekt.db.revier;

import java.io.Serializable;
import java.util.Objects;

public class RevierGebaeudeId implements Serializable {
    private Long revier;
    private Long gebaeude;

    public RevierGebaeudeId() {}

    public RevierGebaeudeId(Long revier, Long gebaeude) {
        this.revier = revier;
        this.gebaeude = gebaeude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevierGebaeudeId that = (RevierGebaeudeId) o;
        return Objects.equals(revier, that.revier) && Objects.equals(gebaeude, that.gebaeude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(revier, gebaeude);
    }
}
