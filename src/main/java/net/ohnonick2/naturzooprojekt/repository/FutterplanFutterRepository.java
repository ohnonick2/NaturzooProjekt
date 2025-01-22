package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterplanFutter;
import net.ohnonick2.naturzooprojekt.db.futter.FutterplanFutterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FutterplanFutterRepository extends JpaRepository<FutterplanFutter, FutterplanFutterId> {

    // Find all entries for a specific FutterPlan
    List<FutterplanFutter> findByFutterplanId(Long futterplanId);

    // Find all entries for a specific Futter
    List<FutterplanFutter> findByFutterId(Long futterId);

    // Custom Query: Find all entries with Menge greater than a specific value
    @Query("SELECT ff FROM futterplan_futter ff WHERE ff.menge > :menge")
    List<FutterplanFutter> findAllWithMengeGreaterThan(int menge);

    // Custom Query: Find all entries for a FutterPlan with their associated Futter
    @Query("SELECT ff FROM futterplan_futter ff JOIN FETCH ff.futter WHERE ff.futterplan.id = :futterplanId")
    List<FutterplanFutter> findDetailedEntriesByFutterplanId(Long futterplanId);
}
