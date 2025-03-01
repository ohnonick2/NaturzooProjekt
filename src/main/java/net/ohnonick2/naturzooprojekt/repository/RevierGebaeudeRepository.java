package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierGebaeude;
import net.ohnonick2.naturzooprojekt.db.revier.RevierGebaeudeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevierGebaeudeRepository extends JpaRepository<RevierGebaeude, RevierGebaeudeId> {

    List<RevierGebaeude> findAllByRevier(Revier revier);

    boolean existsByGebaeude(Gebaeude gebaeude);

    void deleteByGebaeude(Gebaeude gebaeude);

    void deleteByRevier(Revier revier);

}
