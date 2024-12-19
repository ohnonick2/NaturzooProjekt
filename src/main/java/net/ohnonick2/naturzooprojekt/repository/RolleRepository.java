package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolleRepository extends JpaRepository<Rolle, Long> {

    Rolle findRolleById(Long id);
    Rolle findRolleByName(String name);
}
