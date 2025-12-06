package admin.gestao.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiKeyRequestDTO {

    private String cliente;   // Nome do cliente ou empresa
    private String endpoint;  // Endpoint da API
    private String idUser;    // ID do usuário associado (UUID)


}
