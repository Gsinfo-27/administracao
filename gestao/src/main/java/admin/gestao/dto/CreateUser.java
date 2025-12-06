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
public class CreateUser {
    private String userName;
    private String senha;
    private Role role;
    private UUID id;
    private String name;
    private String lastName;
    private String email;

}
