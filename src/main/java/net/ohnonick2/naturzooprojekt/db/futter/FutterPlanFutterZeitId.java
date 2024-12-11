package net.ohnonick2.naturzooprojekt.db.futter;

import java.io.Serializable;
import java.util.Objects;

public class FutterPlanFutterZeitId implements Serializable {

    private Long futterZeit;
    private Long futterplan;

    public FutterPlanFutterZeitId() {}

    public FutterPlanFutterZeitId(Long futterZeit, Long futterplan) {
        this.futterZeit = futterZeit;
        this.futterplan = futterplan;
    }

    // Getter und Setter
    public Long getFutterZeit() {
        return futterZeit;
    }

    public void setFutterZeit(Long futterZeit) {
        this.futterZeit = futterZeit;
    }

    public Long getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(Long futterplan) {
        this.futterplan = futterplan;
    }

    // equals und hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterPlanFutterZeitId that = (FutterPlanFutterZeitId) o;
        return Objects.equals(futterZeit, that.futterZeit) &&
                Objects.equals(futterplan, that.futterplan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterZeit, futterplan);
    }
}
