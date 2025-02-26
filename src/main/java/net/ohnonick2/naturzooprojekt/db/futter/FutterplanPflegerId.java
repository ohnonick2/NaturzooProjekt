package net.ohnonick2.naturzooprojekt.db.futter;

import java.io.Serializable;
import java.util.Objects;

public class FutterplanPflegerId implements Serializable {

    private Long futterPlan;
    private Long pfleger;

    public FutterplanPflegerId() {}

    public FutterplanPflegerId(Long futterPlan, Long pfleger) {
        this.futterPlan = futterPlan;
        this.pfleger = pfleger;
    }

    public Long getFutterPlan() {
        return futterPlan;
    }

    public Long getPfleger() {
        return pfleger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterplanPflegerId that = (FutterplanPflegerId) o;
        return Objects.equals(futterPlan, that.futterPlan) &&
                Objects.equals(pfleger, that.pfleger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterPlan, pfleger);
    }
}
