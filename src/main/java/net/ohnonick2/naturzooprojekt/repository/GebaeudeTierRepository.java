package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import net.ohnonick2.naturzooprojekt.db.gebaeude.GebaeudeTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GebaeudeTierRepository extends JpaRepository<GebaeudeTier, Long> {


    List<GebaeudeTier> findByGebaeude(Gebaeude gebaeude);



}
