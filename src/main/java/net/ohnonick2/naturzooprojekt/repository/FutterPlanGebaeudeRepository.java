package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanGebaeude;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanGebaeudeId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FutterPlanGebaeudeRepository extends JpaRepository<FutterPlanGebaeude, FutterPlanGebaeudeId> {
    List<FutterPlanGebaeude> findByFutterPlanId(FutterPlan futterPlanId);
}
