package net.ohnonick2.naturzooprojekt.db.futter;

import java.io.Serializable;
import java.util.Objects;

public class FutterplanFutterId implements Serializable {
    private FutterPlan futterplan; // Muss mit dem Attributnamen in der Entitätsklasse übereinstimmen
    private Futter futter;        // Muss mit dem Attributnamen in der Entitätsklasse übereinstimmen

    public FutterplanFutterId() {}

    public FutterplanFutterId(FutterPlan futterplan, Futter futter) {
        this.futterplan = futterplan;
        this.futter = futter;
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

    // hashCode und equals für den zusammengesetzten Primärschlüssel
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutterplanFutterId that = (FutterplanFutterId) o;
        return Objects.equals(futterplan, that.futterplan) && Objects.equals(futter, that.futter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(futterplan, futter);
    }
}
