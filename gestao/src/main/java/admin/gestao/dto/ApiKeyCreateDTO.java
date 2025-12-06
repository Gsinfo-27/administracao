package admin.gestao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyCreateDTO {
    private String cliente;
    private String endpoint;
    private UUID idUser;
}
