package co.com.crediya.api.security;

import co.com.crediya.api.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements WebFilter {
    
    private final AuthServiceClient authServiceClient;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // Rutas públicas
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }
        
        String token = extractToken(exchange);
        if (token == null) {
            return unauthorized(exchange);
        }
        
        return authServiceClient.validateToken(token)
                .flatMap(response -> {
                    if (!response.isValid()) {
                        log.warn("Token inválido para path: {}", path);
                        return unauthorized(exchange);
                    }
                    
                    // Validar roles por método HTTP
                    String method = exchange.getRequest().getMethod().name();
                    if (path.equals("/api/v1/solicitud")) {
                        if ("POST".equals(method) && !isClientRole(response.getRoleId())) {
                            log.warn("Usuario con rol {} intentó crear solicitud", response.getRoleId());
                            return forbidden(exchange);
                        }
                        if ("GET".equals(method) && !isAsesorRole(response.getRoleId())) {
                            log.warn("Usuario con rol {} intentó listar solicitudes", response.getRoleId());
                            return forbidden(exchange);
                        }
                    }
                    
                    // Agregar contexto de usuario
                    exchange.getAttributes().put("userId", response.getUserId());
                    exchange.getAttributes().put("userRole", response.getRoleId());
                    
                    return chain.filter(exchange);
                })
                .onErrorResume(error -> {
                    log.error("Error en validación de token: {}", error.getMessage());
                    return unauthorized(exchange);
                });
    }
    
    private boolean isPublicPath(String path) {
        return path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/webjars/swagger-ui") ||
               path.startsWith("/actuator");
    }
    
    private boolean isClientRole(String roleId) {
        // ID del rol CLIENTE o SOLICITANTE
        return "550e8400-e29b-41d4-a716-446655440002".equals(roleId) || // SOLICITANTE
               "550e8400-e29b-41d4-a716-446655440006".equals(roleId);   // CLIENTE
    }
    
    private boolean isAsesorRole(String roleId) {
        // ID del rol ASESOR
        return "550e8400-e29b-41d4-a716-446655440003".equals(roleId);
    }
    
    private String extractToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    
    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}