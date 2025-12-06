package admin.gestao.repository;

import admin.gestao.auth.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SecurityRepository extends JpaRepository<Security, UUID> {

    Optional<Security> findByKeygen(String keygen);

}
