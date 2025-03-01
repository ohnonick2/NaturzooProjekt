package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanFutterZeit;
import net.ohnonick2.naturzooprojekt.db.futter.FutterplanPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FutterplanPflegerRepository extends JpaRepository<FutterplanPfleger, Long> {

    FutterplanPfleger findByFutterPlanIdAndPflegerId(Long futterPlanId, Long pflegerId);
    List<FutterplanPfleger> findByPflegerId(Long pflegerId);
    List<FutterplanPfleger> findByFutterPlanId(Long futterPlanId);


    FutterplanPfleger findByFutterPlanAndPfleger(FutterPlan futterPlan, Pfleger pfleger);
}
