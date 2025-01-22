package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;
import java.util.Objects;

@Entity(name = "futterplan_futter_zeit")
@IdClass(FutterPlanFutterZeitId.class)
public class FutterPlanFutterZeit {

    @Id
    @ManyToOne
    @JoinColumn(name = "futterzeit_id")
    private FutterZeit futterZeit;

    @Id
    @ManyToOne
    @JoinColumn(name = "futterplan_id")
    private FutterPlan futterplan;

    public FutterPlanFutterZeit() {}

    public FutterPlanFutterZeit(FutterZeit futterZeit, FutterPlan futterplan) {
        this.futterZeit = futterZeit;
        this.futterplan = futterplan;
    }

    // Getter und Setter
    public FutterZeit getFutterZeit() {
        return futterZeit;
    }

    public void setFutterZeit(FutterZeit futterZeit) {
        this.futterZeit = futterZeit;
    }

    public FutterPlan getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(FutterPlan futterplan) {
        this.futterplan = futterplan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterPlanFutterZeit that = (FutterPlanFutterZeit) o;
        return Objects.equals(futterZeit, that.futterZeit) &&
                Objects.equals(futterplan, that.futterplan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterZeit, futterplan);
    }
}
