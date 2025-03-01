package net.ohnonick2.naturzooprojekt.db.futter;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "futter_plan_gebaeude")
@IdClass(FutterPlanGebaeudeId.class)
public class FutterPlanGebaeude implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "futter_plan_id", nullable = false)
    private FutterPlan futterPlanId;

    @Id
    @ManyToOne
    @JoinColumn(name = "gebaeude_id", nullable = false)
    private Gebaeude gebaeudeId;

    // 🛠 Standard-Konstruktoren
    public FutterPlanGebaeude() {}

    public FutterPlanGebaeude(FutterPlan futterPlanId, Gebaeude gebaeudeId) {
        this.futterPlanId = futterPlanId;
        this.gebaeudeId = gebaeudeId;
    }

    // 🛠 Getter & Setter
    public FutterPlan getFutterPlanId() {
        return futterPlanId;
    }

    public void setFutterPlanId(FutterPlan futterPlan) {
        this.futterPlanId = futterPlan;
    }

    public Gebaeude getGebaeudeId() {
        return gebaeudeId;
    }

    public void setGebaeudeId(Gebaeude gebaeude) {
        this.gebaeudeId = gebaeude;
    }

    // 🛠 equals & hashCode (für Hibernate)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterPlanGebaeude that = (FutterPlanGebaeude) o;
        return Objects.equals(futterPlanId, that.futterPlanId) &&
                Objects.equals(gebaeudeId, that.gebaeudeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterPlanId, gebaeudeId);
    }

    @Override
    public String toString() {
        return "FutterPlanGebaeude{" +
                "futterPlan=" + futterPlanId.getName() +
                ", gebaeude=" + gebaeudeId.getName() +
                '}';
    }
}
