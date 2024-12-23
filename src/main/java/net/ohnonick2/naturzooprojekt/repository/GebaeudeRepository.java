package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GebaeudeRepository extends JpaRepository<Gebaeude, Long> {

    Gebaeude findByName(String name);
    Gebaeude findById(long id);


}
