package admin.gestao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyResponseDTO {
    private UUID id;
    private String keygen;
    private String cliente;
    private String endpoint;
    private boolean status;
    private LocalDateTime dateTime;
    private UUID idUser;
}

