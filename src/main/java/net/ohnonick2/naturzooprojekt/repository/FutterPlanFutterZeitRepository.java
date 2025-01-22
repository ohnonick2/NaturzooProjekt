package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanFutterZeit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FutterPlanFutterZeitRepository extends JpaRepository<FutterPlanFutterZeit, Long> {

    List<FutterPlanFutterZeit> findByFutterplanId(Long futterplanId);

}
