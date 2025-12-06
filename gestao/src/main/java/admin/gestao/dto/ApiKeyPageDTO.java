package admin.gestao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyPageDTO {
    private List<ApiKeyResponseDTO> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}

