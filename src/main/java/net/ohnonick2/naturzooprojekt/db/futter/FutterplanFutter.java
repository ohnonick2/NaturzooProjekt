package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;

@Entity(name = "futterplan_futter")
@IdClass(FutterplanFutterId.class)
public class FutterplanFutter {

    @Id
    @ManyToOne
    @JoinColumn(name = "futterplan_id", nullable = false)
    private FutterPlan futterplan;

    @Id
    @ManyToOne
    @JoinColumn(name = "futter_id", nullable = false)
    private Futter futter;

    private int menge;


    // Konstruktoren
    public FutterplanFutter() {}

    public FutterplanFutter(FutterPlan futterplan, Futter futter, int menge) {
        this.futterplan = futterplan;
        this.futter = futter;
        this.menge = menge;
    }

    // Getter und Setter
    public FutterPlan getFutterplan() {
        return futterplan;
    }

    public void setFutterplan(FutterPlan futterplan) {
        this.futterplan = futterplan;
    }

    public Futter getFutter() {
        return futter;
    }

    public void setFutter(Futter futter) {
        this.futter = futter;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        if (menge < 0) {
            throw new IllegalArgumentException("Menge darf nicht negativ sein");
        }
        this.menge = menge;
    }

}
