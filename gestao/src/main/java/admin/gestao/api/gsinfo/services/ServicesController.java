package admin.gestao.api.gsinfo.services;


import admin.gestao.api.gsinfo.setings.ApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test-apiConfiguration")
public class ServicesController {

    @Autowired
    private ApiConfiguration apiConfiguration;

    // ============================
    // PING
    // ============================

    @GetMapping("/status")
    public Mono<ResponseEntity<Map<String, Map<String, String>>>> checkAllEndpoints() {
        Map<String, Map<String, String>> report = new HashMap<>();
        String[] endpoints = {"ping", "temRegisto", "ultimaActualizacao", "sessao", "listarTodos", "teste"};

        // Testa client1
        return testClient("client1", false, endpoints, report)
                // Testa client2
                .then(testClient("client2", true, endpoints, report))
                // Retorna o relatório completo
                .then(Mono.just(ResponseEntity.ok(report)));
    }

    private Mono<Void> testClient(String clientName, boolean useClient2, String[] endpoints, Map<String, Map<String, String>> report) {
        Map<String, String> clientReport = new HashMap<>();
        return Flux.fromArray(endpoints)
                .flatMap(endpoint -> {
                    Mono<String> call;
                    try {
                        switch (endpoint) {
                            case "ping":
                                call = apiConfiguration.ping(useClient2);
                                break;
                            case "temRegisto":
                                call = apiConfiguration.temRegisto(useClient2).map(String::valueOf);
                                break;
                            case "ultimaActualizacao":
                                call = apiConfiguration.ultimaActualizacao(useClient2);
                                break;
                            case "sessao":
                                call = apiConfiguration.sessao("teste", useClient2);
                                break;
                            case "listarTodos":
                                call = apiConfiguration.listarTodosUsuarios(useClient2).map(List::toString);
                                break;
                            case "teste":
                                call = apiConfiguration.teste(Map.of("teste", "ok"), useClient2).map(Map::toString);
                                break;
                            default:
                                call = Mono.just("não testado");
                        }
                    } catch (Exception e) {
                        call = Mono.just("erro: " + e.getMessage());
                    }

                    return call
                            .timeout(Duration.ofSeconds(5))
                            .onErrorResume(err -> Mono.just("offline: " + err.getMessage()))
                            .map(status -> Map.entry(endpoint, status));
                })
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .doOnNext(clientMap -> report.put(clientName, clientMap))
                .then();
    }
    // Registrar usuário
    // ============================
    @PostMapping("/registrar/{client}")
    public Mono<ResponseEntity<Map>> registrarUsuario(@PathVariable("client") String client,
                                                      @RequestBody Map<String, Object> registroRequest) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.registrarUsuario(registroRequest, useClient2)
                .map(resp -> ResponseEntity.ok(resp))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body(Map.of("erro", err.getMessage()))));
    }

    // ============================
    // Login
    // ============================
    @PostMapping("/login/{client}")
    public Mono<ResponseEntity<Map>> login(@PathVariable("client") String client,
                                           @RequestBody Map<String, Object> loginRequest) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.login(loginRequest, useClient2)
                .map(resp -> ResponseEntity.ok(resp))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body(Map.of("erro", err.getMessage()))));
    }

    // ============================
    // Alterar senha
    // ============================
    @PostMapping("/alterar-senha/{client}")
    public Mono<ResponseEntity<Object>> alterarSenha(@PathVariable("client") String client,
                                                     @RequestParam String usuario,
                                                     @RequestParam String senhaAntiga,
                                                     @RequestParam String senhaNova) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.alterarSenha(usuario, senhaAntiga, senhaNova, useClient2)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    // ============================
    // Registrar usuários em bulk
    // ============================
    @PostMapping("/registrar-bulk/{client}")
    public Mono<ResponseEntity<Object>> registrarUsuariosBulk(@PathVariable("client") String client,
                                                              @RequestBody List<Map<String, Object>> usuariosList) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.registrarUsuariosBulk(List.copyOf(usuariosList), useClient2)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    // ============================
    // Atualizar usuários em bulk
    // ============================
    @PutMapping("/atualizar-bulk/{client}")
    public Mono<ResponseEntity<Object>> atualizarUsuariosBulk(@PathVariable("client") String client,
                                                              @RequestBody List<Map<String, Object>> usuariosList) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.atualizarUsuariosBulk(List.copyOf(usuariosList), useClient2)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    // ============================
    // Check session
    // ============================
    @GetMapping("/check-session/{client}")
    public Mono<ResponseEntity<Map>> checkSession(@PathVariable("client") String client,
                                                  @RequestParam String userName) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.checkSession(userName, useClient2)
                .map(resp -> ResponseEntity.ok(resp))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body(Map.of("erro", err.getMessage()))));
    }

    // ============================
    // Sessao
    // ============================
    @GetMapping("/sessao/{client}")
    public Mono<ResponseEntity<String>> sessao(@PathVariable("client") String client,
                                               @RequestParam String nome) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.sessao(nome, useClient2)
                .map(resp -> ResponseEntity.ok(resp))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body("Erro: " + err.getMessage())));
    }

    // ============================
    // Tem registro?
    // ============================
    @GetMapping("/tem-registo/{client}")
    public Mono<ResponseEntity<Boolean>> temRegisto(@PathVariable("client") String client) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.temRegisto(useClient2)
                .map(resp -> ResponseEntity.ok(resp))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body(false)));
    }

    // ============================
    // Última atualização
    // ============================
    @GetMapping("/ultima-actualizacao/{client}")
    public Mono<ResponseEntity<String>> ultimaActualizacao(@PathVariable("client") String client) {
        boolean useClient2 = client.equalsIgnoreCase("client2");
        return apiConfiguration.ultimaActualizacao(useClient2)
                .map(resp -> ResponseEntity.ok(resp))
                .onErrorResume(err -> Mono.just(ResponseEntity.internalServerError().body("Erro: " + err.getMessage())));
    }

    @GetMapping("/listar-todos")
    public Mono<ResponseEntity<Map<String, List>>> listarTodosClientes() {
        // Chama os dois serviços em paralelo
        Mono<List> client1Mono = apiConfiguration.listarTodosUsuarios(false)
                .onErrorResume(err -> Mono.just(List.of())); // se der erro, retorna lista vazia

        Mono<List> client2Mono = apiConfiguration.listarTodosUsuarios(true)
                .onErrorResume(err -> Mono.just(List.of())); // idem

        // Combina os resultados em um Map
        return Mono.zip(client1Mono, client2Mono)
                .map(tuple -> {
                    Map<String, List> result = Map.of(
                            "cliente1", tuple.getT1(),
                            "cliente2", tuple.getT2()
                    );
                    return ResponseEntity.ok(result);
                });
    }


}
