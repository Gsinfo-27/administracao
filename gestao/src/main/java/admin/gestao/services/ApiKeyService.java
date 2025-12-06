package admin.gestao.services;



import admin.gestao.auth.ApiKey;
import admin.gestao.dto.ApiKeyCreateDTO;
import admin.gestao.dto.ApiKeyPageDTO;
import admin.gestao.dto.ApiKeyResponseDTO;
import admin.gestao.dto.ApiKeyValidationDTO;
import admin.gestao.repository.ApiKeyRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiKeyService {

    private final ApiKeyRepository repository;

    public ApiKeyService(ApiKeyRepository repository) {
        this.repository = repository;
    }

    public ApiKeyResponseDTO create(ApiKeyCreateDTO dto) {
        ApiKey entity = new ApiKey();
        entity.setKeygen(UUID.randomUUID().toString());
        entity.setCliente(dto.getCliente());
        entity.setEndpoint(dto.getEndpoint());
        entity.setStatus(true);
        entity.setDateTime(LocalDateTime.now());
        entity.setIdUser(dto.getIdUser());

        ApiKey saved = repository.save(entity);
        return toDto(saved);
    }

    public ApiKeyResponseDTO toDto(ApiKey a) {
        ApiKeyResponseDTO r = new ApiKeyResponseDTO();
        r.setId(a.getId());
        r.setKeygen(a.getKeygen());
        r.setCliente(a.getCliente());
        r.setEndpoint(a.getEndpoint());
        r.setStatus(a.isStatus());
        r.setDateTime(a.getDateTime());
        r.setIdUser(a.getIdUser());
        return r;
    }

    public ApiKeyPageDTO list(String search, String status, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, toSort(sort));
        Specification<ApiKey> spec = buildSpec(search, status);

        Page<ApiKey> result = repository.findAll(spec, pageable);

        ApiKeyPageDTO pageDto = new ApiKeyPageDTO();
        pageDto.setContent(result.getContent().stream().map(this::toDto).collect(Collectors.toList()));
        pageDto.setTotalElements(result.getTotalElements());
        pageDto.setTotalPages(result.getTotalPages());
        pageDto.setPage(result.getNumber());
        pageDto.setSize(result.getSize());
        return pageDto;
    }

    private Sort toSort(String sort) {
        if (sort == null || sort.isBlank()) return Sort.by(Sort.Direction.DESC, "dateTime");
        switch (sort) {
            case "date-asc": return Sort.by(Sort.Direction.ASC, "dateTime");
            case "date-desc": return Sort.by(Sort.Direction.DESC, "dateTime");
            case "client-asc": return Sort.by(Sort.Direction.ASC, "cliente");
            case "client-desc": return Sort.by(Sort.Direction.DESC, "cliente");
            default: return Sort.by(Sort.Direction.DESC, "dateTime");
        }
    }

    private Specification<ApiKey> buildSpec(String search, String status) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("cliente")), like),
                                cb.like(cb.lower(root.get("endpoint")), like),
                                cb.like(cb.lower(root.get("keygen")), like)
                        )
                );
            }

            if (status != null && !status.isBlank()) {
                if ("active".equalsIgnoreCase(status)) {
                    predicates.add(cb.isTrue(root.get("status")));
                } else if ("inactive".equalsIgnoreCase(status)) {
                    predicates.add(cb.isFalse(root.get("status")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public boolean updateStatus(String keygen, boolean activate) {
        Optional<ApiKey> o = repository.findByKeygen(keygen);
        if (o.isEmpty()) return false;
        ApiKey k = o.get();
        k.setStatus(activate);
        repository.save(k);
        return true;
    }

    public boolean deleteByKeygen(String keygen) {
        if (!repository.existsByKeygen(keygen)) return false;
        repository.deleteByKeygen(keygen);
        return true;
    }

    public ApiKeyValidationDTO validate(String keygen) {
        ApiKeyValidationDTO res = new ApiKeyValidationDTO();
        Optional<ApiKey> o = repository.findByKeygen(keygen);
        if (o.isEmpty()) {
            res.setValid(false);
            res.setActive(false);
            return res;
        }
        ApiKey k = o.get();
        res.setValid(true);
        res.setActive(k.isStatus());
        res.setCliente(k.getCliente());
        res.setEndpoint(k.getEndpoint());
        return res;
    }

    public Optional<ApiKey> getByKey(String keygen) {
        return repository.findByKeygen(keygen);
    }


    public ApiKeyResponseDTO convertToDTO(ApiKey entity) {
        ApiKeyResponseDTO dto = new ApiKeyResponseDTO();
        dto.setId(entity.getId());
        dto.setKeygen(entity.getKeygen());
        dto.setCliente(entity.getCliente());
        dto.setEndpoint(entity.getEndpoint());
        dto.setStatus(entity.isStatus());
        dto.setDateTime(entity.getDateTime());
        dto.setIdUser(entity.getIdUser());
        return dto;
    }

}
