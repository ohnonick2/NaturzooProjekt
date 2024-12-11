package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Pflegerrepository extends JpaRepository<Pfleger, Long> {

    Pfleger findPflegerById(Long id);

    Pfleger findByBenutzername(String benutzername);
}
