package net.ohnonick2.naturzooprojekt.repository;

import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolleUserRepository extends JpaRepository<RolleUser, Long> {

    RolleUser findByUserId(Long userId);



}
