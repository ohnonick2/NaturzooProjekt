package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Tierartrepository extends JpaRepository<TierArt, Long> {

    TierArt findByName(String name);
    List<TierArt> findAll();
    Optional<TierArt> findById(Long id);
}
