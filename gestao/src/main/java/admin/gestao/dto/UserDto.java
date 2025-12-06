package admin.gestao.dto;

import java.util.UUID;

public record UserDto(UUID userId,
                     String username,
                     String email,
                     String imageUrl) {}
