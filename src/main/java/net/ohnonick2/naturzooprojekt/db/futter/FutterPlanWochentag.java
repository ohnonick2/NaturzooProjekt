package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;

@Entity(name = "futterplan_wochentag")
@IdClass(FutterPlanWochentagId.class)
public class FutterPlanWochentag {

    @Id
    @ManyToOne
    @JoinColumn(name = "futterplan_id", nullable = false)
    private FutterPlan futterplan;

    @Id
    @ManyToOne
    @JoinColumn(name = "wochentag_id", nullable = false)
    private Wochentag wochentag;

    // Konstruktoren
    public FutterPlanWochentag() {}

    public FutterPlanWochentag(FutterPlan futterplan, Wochentag wochentag) {
        this.futterplan = futterplan;
        this.wochentag = wochentag;
    }

    // Getter und Setter
    public FutterPlan getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(FutterPlan futterplan) {
        this.futterplan = futterplan;
    }

    public Wochentag getWochentag() {
        return wochentag;
    }

    public void setWochentag(Wochentag wochentag) {
        this.wochentag = wochentag;
    }
}
