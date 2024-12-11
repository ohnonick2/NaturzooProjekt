package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;

@Entity(name = "futterplan_futter_zeit")
@IdClass(FutterPlanFutterZeitId.class)
public class FutterPlanFutterZeit {

    @Id
    @OneToOne
    @JoinColumn(name = "futterzeit_id") // Spaltenname in der Datenbank
    private FutterZeit futterZeit;

    @Id
    @OneToOne
    @JoinColumn(name = "futterplan_id") // Spaltenname in der Datenbank
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
}
