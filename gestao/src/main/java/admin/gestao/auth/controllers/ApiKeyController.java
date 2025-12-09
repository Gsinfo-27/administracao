package admin.gestao.auth.controllers;


import admin.gestao.dto.ApiKeyCreateDTO;
import admin.gestao.dto.ApiKeyPageDTO;
import admin.gestao.dto.ApiKeyResponseDTO;
import admin.gestao.auth.services.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    @Autowired
    private ApiKeyService service;

    // ============================================================
    // 1. CRIAR API KEY
    // ============================================================
    @PostMapping
    public ResponseEntity<ApiKeyResponseDTO> create(@RequestBody ApiKeyCreateDTO dto) {

        return ResponseEntity.ok(service.create(dto));
    }

    // ============================================================
    // 2. LISTAR COM PAGINAÇÃO, FILTRO E ORDEM
    // ============================================================
    @GetMapping
    public ResponseEntity<ApiKeyPageDTO> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiKeyPageDTO result = service.list(search, status, sort, page, size);
        return ResponseEntity.ok(result);
    }


    // ============================================================
    // 3. BUSCAR UMA API KEY PELO KEYGEN
    // ============================================================
    @GetMapping("/{keygen}")
    public ResponseEntity<ApiKeyResponseDTO> findOne(@PathVariable String keygen) {
        return service.getByKey(keygen)
                .map(service::convertToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{keygen}/status/{newStatus}")
    public ResponseEntity<Boolean> updateStatus(
            @PathVariable String keygen,
            @PathVariable boolean newStatus
    ) {
        return ResponseEntity.ok(service.updateStatus(keygen, newStatus));
    }

    // ============================================================
    // 5. REMOVER API KEY
    // ============================================================
    @DeleteMapping("/{keygen}")
    public ResponseEntity<String> delete(@PathVariable String keygen) {
        service.deleteByKeygen(keygen);
        return ResponseEntity.ok("API Key removida com sucesso.");
    }
}
