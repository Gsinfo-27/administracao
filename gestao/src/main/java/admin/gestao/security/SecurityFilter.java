package admin.gestao.security;

import admin.gestao.repository.UserRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Component
public class SecurityFilter extends OncePerRequestFilter {

        @Autowired
        private JWTGen tokenService;

        @Autowired
        private UserRepository userRepository;

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            // Se é uma rota que não precisa de autenticação, passa direto
            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                String token = recoverToken(request);

                if (token == null) {
                    sendUnauthorizedError(response, "Token não fornecido");
                    return;
                }

                DecodedJWT jwt = tokenService.decodeToken(token);
                if (jwt == null) {
                    sendUnauthorizedError(response, "Token inválido");
                    return;
                }

                String login = jwt.getSubject();
                String role = jwt.getClaim("role").asString();

                if (login == null || login.isEmpty()) {
                    sendUnauthorizedError(response, "Token não contém um Login válido");
                    return;
                }

                UserDetails user = userRepository.findByUserName(login);
                if (user == null) {
                    sendUnauthorizedError(response, "Usuário não encontrado");
                    return;
                }

                // Cria a autenticação
                var authorities = List.of(new SimpleGrantedAuthority(role));
                var authentication = new UsernamePasswordAuthenticationToken(
                        user, null, authorities);

                // Define o contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                logger.error("Falha na autenticação JWT: {}", ex);
                sendUnauthorizedError(response, "Autenticação falhou: " + ex.getMessage());
                return;
            }

            filterChain.doFilter(request, response);
        }

        private String recoverToken(HttpServletRequest request) {
            var authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }
            return authHeader.replace("Bearer ", "");
        }

        private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            String path = request.getRequestURI();
            return path.startsWith("/api/keys") ||
                    path.startsWith("/api/user") ||
                    path.startsWith("/api/security/access") ||
                    path.startsWith("/api/test-services");

        }


}
