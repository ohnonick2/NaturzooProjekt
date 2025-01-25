package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LieferantRepository extends JpaRepository<Lieferant, Long> {

    Lieferant findLieferantById(Long id);

}
