package admin.gestao.dto;


import java.util.UUID;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ProfileImageDTO {
    private UUID userId;
    private String imageBase64; // Imagem enviada em Base64
}


