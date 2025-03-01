package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WochenTagRepository extends JpaRepository<Wochentag, Long> {

    Wochentag findWochentagById(Long id);
    Wochentag findWochentagByName(String name);
}
