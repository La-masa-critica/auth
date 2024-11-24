package LMC.auth.repositories;

import LMC.auth.models.AuthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthDataRepository extends JpaRepository<AuthData, Long> {
    Optional<AuthData> findByUsername(String username);
}
