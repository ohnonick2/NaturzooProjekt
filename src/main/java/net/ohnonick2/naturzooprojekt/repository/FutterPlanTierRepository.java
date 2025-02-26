package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FutterPlanTierRepository extends JpaRepository<FutterPlanTier, Long> {

    List<FutterPlanTier> findByFutterplan(FutterPlan futterplan);
}
