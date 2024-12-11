package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.futter.Futter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutterRepositority extends JpaRepository<Futter, Long> {

    Futter findFutterById(Long id);
}
