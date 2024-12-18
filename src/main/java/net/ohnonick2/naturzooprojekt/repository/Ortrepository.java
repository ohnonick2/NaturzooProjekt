package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface Ortrepository extends JpaRepository<Ort, Long> {

    Ort findByPlz(Integer plz);

    List<Ort> findByOrtnameAndPlz(String ortname , Integer plz);
    List<Ort> findByOrtnameOrPlz(String ortname , Integer plz);
}
