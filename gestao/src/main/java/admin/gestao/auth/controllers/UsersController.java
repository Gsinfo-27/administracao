package admin.gestao.auth.controllers;

import admin.gestao.dto.*;
import admin.gestao.security.JWTGen;
import admin.gestao.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTGen generateToken;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Void> registrarUsuario(@RequestBody CreateUser createUser) {
        userService.create(createUser);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login loginRequest) {

        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                var token = generateToken.generateToken(
                        userService.findByName(loginRequest.getUserName()),
                        LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.of("+02:00"))
                );

                return ResponseEntity.ok(new LoginResponse(token, "Sucesso"));
            }

            return ResponseEntity.status(401).body(new LoginResponse(null, "Credenciais inválidas"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new LoginResponse(null, "Usuário ou senha inválidos!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new LoginResponse(null, "Erro interno no servidor."));
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<UserListDTO> checkLoginStatus(@RequestParam("userName") String userName) {
        return ResponseEntity.ok(userService.findByNameDto(userName));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@ModelAttribute ProfileImageDTO dto) throws IOException {
        userService.updateProfileImage(dto);
        return ResponseEntity.ok("Imagem atualizada com sucesso!");
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserListDTO>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuário removido!");
    }

    @GetMapping("/conect")
    public ResponseEntity<Boolean>connect(){
        return ResponseEntity.ok(true);
    }
}
