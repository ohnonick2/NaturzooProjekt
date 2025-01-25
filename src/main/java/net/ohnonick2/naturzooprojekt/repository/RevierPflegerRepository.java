package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPflegerId;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevierPflegerRepository extends JpaRepository<RevierPfleger, RevierPflegerId> {
    List<RevierPfleger> findAllByRevier(Revier revier);
    List<RevierPfleger> findAllByPfleger(Pfleger pfleger);
    RevierPfleger findByRevierAndPfleger(Revier revier, Pfleger pfleger);


}
