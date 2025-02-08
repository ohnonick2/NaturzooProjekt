package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.FutterZeit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutterZeitRepository extends JpaRepository<FutterZeit, Long> {

    FutterZeit findFutterZeitById(Long id);

    FutterZeit findFutterZeitByuhrzeit(String uhrzeit);

}
