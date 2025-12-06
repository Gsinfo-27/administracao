package admin.gestao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyValidationDTO {
    private boolean valid;
    private boolean active;
    private String cliente;
    private String endpoint;
}
