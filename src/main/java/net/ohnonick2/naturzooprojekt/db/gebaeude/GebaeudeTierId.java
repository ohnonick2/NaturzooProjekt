package net.ohnonick2.naturzooprojekt.db.gebaeude;

import java.io.Serializable;
import java.util.Objects;

import java.io.Serializable;
import java.util.Objects;

public class GebaeudeTierId implements Serializable {

    private Long gebaeude; // Der Name muss mit `GebaeudeTier.gebaeude` übereinstimmen
    private Long tier;     // Der Name muss mit `GebaeudeTier.tier` übereinstimmen

    public GebaeudeTierId() {
    }

    public GebaeudeTierId(Long gebaeude, Long tier) {
        this.gebaeude = gebaeude;
        this.tier = tier;
    }

    // Getter und Setter
    public Long getGebaeude() {
        return gebaeude;
    }

    public void setGebaeude(Long gebaeude) {
        this.gebaeude = gebaeude;
    }

    public Long getTier() {
        return tier;
    }

    public void setTier(Long tier) {
        this.tier = tier;
    }

    // hashCode und equals müssen überschrieben werden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GebaeudeTierId that = (GebaeudeTierId) o;
        return Objects.equals(gebaeude, that.gebaeude) && Objects.equals(tier, that.tier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gebaeude, tier);
    }
}
