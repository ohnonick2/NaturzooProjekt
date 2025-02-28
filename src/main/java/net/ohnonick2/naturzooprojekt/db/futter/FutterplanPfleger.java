package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;

@Entity
@Table(name = "futterplan_pfleger")
@IdClass(FutterplanPflegerId.class)
public class FutterplanPfleger {

    @Id
    @ManyToOne
    @JoinColumn(name = "futterplan_id")
    private FutterPlan futterPlan;

    @Id
    @ManyToOne
    @JoinColumn(name = "pfleger_id")
    private Pfleger pfleger;

    public FutterplanPfleger() {}

    public FutterplanPfleger(FutterPlan futterPlan, Pfleger pfleger) {
        this.futterPlan = futterPlan;
        this.pfleger = pfleger;
    }

    public FutterPlan getFutterPlan() {
        return futterPlan;
    }

    public Pfleger getPfleger() {
        return pfleger;
    }
}
