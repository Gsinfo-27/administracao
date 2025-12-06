package admin.gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class Services {

    private final WebClient client1;
    private final WebClient client2;

    public Services() {
        // Configura ObjectMapper para datas (opcional)
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Apenas cria os WebClients, sem custom encoder/decoder
        this.client1 = WebClient.builder()
                .baseUrl("https://api-restaurante-1-6u7x.onrender.com")
                .build();

        this.client2 = WebClient.builder()
                .baseUrl("https://api-restaurante-rirc.onrender.com")
                .build();
    }

    public WebClient api1() { return client1; }
    public WebClient api2() { return client2; }

    // ========================================
    // Métodos para chamar UsuarioController
    // ========================================

    // Registrar usuário
    public Mono<Map> registrarUsuario(Object registroRequest, boolean useClient2) {
        return (useClient2 ? client2 : client1).post()
                .uri("/api/user/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registroRequest)
                .retrieve()
                .bodyToMono(Map.class);
    }

    // Alterar senha
    public Mono<Void> alterarSenha(String usuario, String senhaAntiga, String senhaNova, boolean useClient2) {
        return (useClient2 ? client2 : client1).post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/user/alterar-senha")
                        .queryParam("Usuario", usuario)
                        .queryParam("senhaAntiga", senhaAntiga)
                        .queryParam("senhaNova", senhaNova)
                        .build())
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Registrar usuários em bulk
    public Mono<Void> registrarUsuariosBulk(List<Object> usuariosList, boolean useClient2) {
        return (useClient2 ? client2 : client1).post()
                .uri("/api/user/registrarBulk")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(usuariosList)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Atualizar usuários em bulk
    public Mono<Void> atualizarUsuariosBulk(List<Object> usuariosList, boolean useClient2) {
        return (useClient2 ? client2 : client1).put()
                .uri("/api/user/actualizarBulk")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(usuariosList)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Login
    public Mono<Map> login(Object loginRequest, boolean useClient2) {
        return (useClient2 ? client2 : client1).post()
                .uri("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(Map.class);
    }

    // Check session
    public Mono<Map> checkSession(String userName, boolean useClient2) {
        return (useClient2 ? client2 : client1).get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/user/check-session")
                        .queryParam("userName", userName)
                        .build())
                .retrieve()
                .bodyToMono(Map.class);
    }

    // Sessão
    public Mono<String> sessao(String nome, boolean useClient2) {
        return (useClient2 ? client2 : client1).get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/user/sessao")
                        .queryParam("nome", nome)
                        .build())
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Tem registro?
    public Mono<Boolean> temRegisto(boolean useClient2) {
        return (useClient2 ? client2 : client1).get()
                .uri("/api/user/tem-registo")
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    // Teste endpoint
    public Mono<Map> teste(Map<String, Object> map, boolean useClient2) {
        return (useClient2 ? client2 : client1).post()
                .uri("/api/user/teste")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(map)
                .retrieve()
                .bodyToMono(Map.class);
    }

    // Ping
    public Mono<String> ping(boolean useClient2) {
        return (useClient2 ? client2 : client1).get()
                .uri("/api/user/ping")
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Última atualização
    public Mono<String> ultimaActualizacao(boolean useClient2) {
        return (useClient2 ? client2 : client1).get()
                .uri("/api/user/ultimaActualizacao")
                .retrieve()
                .bodyToMono(String.class);
    }

    // Listar todos usuários
    public Mono<List> listarTodosUsuarios(boolean useClient2) {
        return (useClient2 ? client2 : client1).get()
                .uri("/api/user/listarTodos")
                .retrieve()
                .bodyToMono(List.class);
    }
}
