package admin.gestao.auth.repository;


import admin.gestao.auth.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID>, JpaSpecificationExecutor<ApiKey> {
    Optional<ApiKey> findByKeygen(String keygen);
    void deleteByKeygen(String keygen);
    boolean existsByKeygen(String keygen);
}
