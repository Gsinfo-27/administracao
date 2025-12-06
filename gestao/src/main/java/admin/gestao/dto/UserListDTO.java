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
public class UserListDTO {
    private UUID id;
    private String username;
    private String name;
    private String email;
    private Role role;
    private String lastName;
}
