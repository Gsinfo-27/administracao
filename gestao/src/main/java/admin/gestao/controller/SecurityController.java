package admin.gestao.controller;

import admin.gestao.auth.Security;
import admin.gestao.services.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityService service;

    public SecurityController(SecurityService service) {
        this.service = service;
    }

    // Criar nova chave
    @PostMapping("/create")
    public ResponseEntity<Security> create(@RequestParam String endpoint) {
        Security security = service.createKey(endpoint);
        return ResponseEntity.ok(security);
    }

    // Ativar chave
    @PostMapping("/activate/{keygen}")
    public ResponseEntity<String> activate(@PathVariable String keygen) {
        boolean success = service.activateKey(keygen);
        if (success) {
            return ResponseEntity.ok("Chave ativada com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Chave não encontrada");
        }
    }

    // Desativar chave
    @PostMapping("/deactivate/{keygen}")
    public ResponseEntity<String> deactivate(@PathVariable String keygen) {
        boolean success = service.deactivateKey(keygen);
        if (success) {
            return ResponseEntity.ok("Chave desativada com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Chave não encontrada");
        }
    }

    // Validar chave e retornar endpoint
    @GetMapping("/access/{keygen}")
    public ResponseEntity<String> access(@PathVariable String keygen) {
        return service.getEndpoint(keygen)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(403).body("Chave inválida ou inativa"));
    }
}
