package net.ohnonick2.naturzooprojekt.db.futter;

import java.io.Serializable;
import java.util.Objects;

public class FutterPlanWochentagId implements Serializable {

    private Long futterplan; // Muss zu `futterplan` in `FutterPlanWochentag` passen
    private Long wochentag; // Muss zu `wochentag` in `FutterPlanWochentag` passen

    public FutterPlanWochentagId() {}

    public FutterPlanWochentagId(Long futterplan, Long wochentag) {
        this.futterplan = futterplan;
        this.wochentag = wochentag;
    }

    // Getter und Setter
    public Long getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(Long futterplan) {
        this.futterplan = futterplan;
    }

    public Long getWochentag() {
        return wochentag;
    }

    public void setWochentag(Long wochentag) {
        this.wochentag = wochentag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterPlanWochentagId that = (FutterPlanWochentagId) o;
        return Objects.equals(futterplan, that.futterplan) &&
                Objects.equals(wochentag, that.wochentag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterplan, wochentag);
    }
}
