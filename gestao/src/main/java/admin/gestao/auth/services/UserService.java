package admin.gestao.auth.services;

import admin.gestao.auth.model.Users;
import admin.gestao.api.claudinary.services.ClaudinaryService;
import admin.gestao.dto.ChangePasswordDTO;
import admin.gestao.dto.CreateUser;
import admin.gestao.dto.ProfileImageDTO;
import admin.gestao.dto.UserListDTO;
import admin.gestao.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClaudinaryService claudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users findByName(String name) {
        return userRepository.findByUserName(name);
    }

    @Transactional
    public void create(CreateUser createUser) {
        var pass=createUser.getSenha();
        createUser.setSenha(passwordEncoder.encode(pass));
        userRepository.save(new Users(createUser));
    }


    @Transactional
    public void updateProfileImage(ProfileImageDTO dto) throws IOException {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Converter Base64 → byte[]
        byte[] imageBytes = Base64.getDecoder().decode(dto.getImageBase64());

        // Se o usuário já tinha imagem → apagar do Cloudinary
        if (user.getImagem() != null) {
            String oldPublicId = user.getImagem()
                    .substring(user.getImagem().lastIndexOf("/") + 1)
                    .replace(".png", "")
                    .replace(".jpg", "");
            claudinaryService.deleteImage(oldPublicId);
        }

        // Gerar novo ID único da imagem
        String newPublicId = "perfil_" + user.getId() + "_" + System.currentTimeMillis();

        // Upload para Cloudinary
        String imageUrl = claudinaryService.uploadImage(imageBytes, newPublicId);

        // Gravar URL no banco
        user.setImagem(imageUrl);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        Users user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenhaAtual(), user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(dto.getNovaSenha()));

        userRepository.save(user);
    }


    public List<UserListDTO> listUsers() {
        return userRepository.findByAllUsers().stream()
                .map(user -> {
                    UserListDTO dto = new UserListDTO();
                    dto.setId(user.getId());
                    dto.setName(user.getPerson().getName());
                    dto.setUsername(user.getUserName());
                    dto.setEmail(user.getPerson().getAdress());
                    dto.setLastName(user.getPerson().getLastName());
                    dto.setRole(user.getRole());
                    return dto;
                })
                .toList(); // toList() é do Java 16+. Se usar Java <16, use .collect(Collectors.toList())
    }

    public UserListDTO findByNameDto(String name) {
        var user= userRepository.findByUserName(name);
        UserListDTO dto = new UserListDTO();
        dto.setId(user.getId());
        dto.setName(user.getPerson().getName());
        dto.setUsername(user.getUserName());
        dto.setEmail(user.getPerson().getAdress());
        dto.setLastName(user.getPerson().getLastName());
        dto.setRole(user.getRole());
        return dto;
    }

    @Transactional
    public void deleteUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("nenhum usuario encontrado"));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
