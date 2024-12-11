package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Tierrespository extends JpaRepository<Tier , Long> {

    Tier findByName(String name);




}
