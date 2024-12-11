package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanWochentag;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutterPlanWochentagRepository extends JpaRepository<FutterPlanWochentag, Long> {

    FutterPlanWochentag findByFutterplanAndWochentag(FutterPlan futterplan, Wochentag wochentag);

}
