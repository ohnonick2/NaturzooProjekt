package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.adresse.Adresse;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {

    Adresse findAdresseById(Long id);

    Adresse findByStrasseAndHausnummerAndOrt(String strasse, String hausnummer, Ort ort);


}
