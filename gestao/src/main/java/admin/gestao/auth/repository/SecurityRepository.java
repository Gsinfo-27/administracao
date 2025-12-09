package admin.gestao.auth.repository;

import admin.gestao.auth.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SecurityRepository extends JpaRepository<Security, UUID> {

    Optional<Security> findByKeygen(String keygen);

}
