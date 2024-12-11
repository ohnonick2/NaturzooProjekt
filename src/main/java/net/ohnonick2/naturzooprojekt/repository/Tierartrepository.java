package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Tierartrepository extends JpaRepository<TierArt, Long> {

    TierArt findByName(String name);
}
