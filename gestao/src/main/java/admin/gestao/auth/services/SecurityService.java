package admin.gestao.auth.services;

import admin.gestao.auth.model.Security;
import admin.gestao.auth.repository.SecurityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecurityService {

    private final SecurityRepository repository;

    public SecurityService(SecurityRepository repository) {
        this.repository = repository;
    }

    // Criar nova chave para um endpoint
    @Transactional
    public Security createKey(String endpoint) {
        Security security = new Security();
        security.setKeygen(UUID.randomUUID().toString());
        security.setStatus(true); // ativa por padrão
        security.setDateTime(LocalDateTime.now());
        security.setEndpoint(endpoint);
        return repository.save(security);
    }

    // Ativar chave
    @Transactional
    public boolean activateKey(String keygen) {
        Optional<Security> optional = repository.findByKeygen(keygen);
        if (optional.isPresent()) {
            Security security = optional.get();
            security.setStatus(true);
            repository.save(security);
            return true;
        }
        return false;
    }

    // Desativar chave
    @Transactional
    public boolean deactivateKey(String keygen) {
        Optional<Security> optional = repository.findByKeygen(keygen);
        if (optional.isPresent()) {
            Security security = optional.get();
            security.setStatus(false);
            repository.save(security);
            return true;
        }
        return false;
    }

    // Validar chave e retornar endpoint
    public Optional<String> getEndpoint(String keygen) {
        return repository.findByKeygen(keygen)
                .filter(Security::getStatus) // só retorna se a chave estiver ativa
                .map(Security::getEndpoint);
    }
}
