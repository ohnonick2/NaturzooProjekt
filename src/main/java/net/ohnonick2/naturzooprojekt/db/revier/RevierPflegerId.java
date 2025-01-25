package net.ohnonick2.naturzooprojekt.db.revier;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RevierPflegerId implements Serializable {

    private Long revier;
    private Long pfleger;

    // Standardkonstruktor
    public RevierPflegerId() {}

    public RevierPflegerId(Long revierId, Long pflegerId) {
        this.revier = revierId;
        this.pfleger = pflegerId;
    }

    // Getter und Setter
    public Long getRevierId() {
        return revier;
    }

    public void setRevierId(Long revierId) {
        this.revier = revierId;
    }

    public Long getPflegerId() {
        return pfleger;
    }

    public void setPflegerId(Long pflegerId) {
        this.pfleger = pflegerId;
    }

    // equals und hashCode (wichtig für zusammengesetzte Schlüssel)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevierPflegerId that = (RevierPflegerId) o;
        return Objects.equals(revier, that.revier) &&
                Objects.equals(pfleger, that.pfleger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(revier, pfleger);
    }
}
