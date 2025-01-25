package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FutterPlanRepository extends JpaRepository<FutterPlan, Long> {

    FutterPlan findFutterPlanById(Long id);



}
