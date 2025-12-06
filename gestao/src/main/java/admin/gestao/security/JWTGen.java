package admin.gestao.security;

import admin.gestao.auth.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
@Service
public class JWTGen {

    @Value("${jwt.security.token.secret}")
    private String secret;

    private static final String ISSUER = "api-restaurante";

    public String generateToken(Users usuario, Instant expiration) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        // Pega só uma permissão (por exemplo, a primeira da lista)
        String rolePrincipal = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");  // padrão caso não tenha

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getUsername())
                .withClaim("role", rolePrincipal)  // claim única "role"
                .withExpiresAt(getDefaultExpirationDate())
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            exception.printStackTrace(); // imprime o erro no console
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("role").asString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair role do token", e);
        }
    }

    public String getSubjectFromToken(String token) {
        DecodedJWT jwt = decodeToken(token);
        return (jwt != null) ? jwt.getSubject() : null;
    }

    private Instant getDefaultExpirationDate() {
        return LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.of("+02:00"));
    }
}
