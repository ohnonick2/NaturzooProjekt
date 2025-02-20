package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTierId;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevierTierRepository extends JpaRepository<RevierTier, Long> {

    // Sucht nach einer bestimmten Tier-Revier-Zuordnung
    RevierTier findByRevierIdAndTierId(Revier revierId, Tier tierId);

    // Sucht alle Tiere, die zu einem Revier gehören
    List<RevierTier> findAllByRevierId(Revier revierId);

    RevierTier findByTierId(Tier tierId);

}
