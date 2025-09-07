package co.com.crediya.api.security;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SecurityUtils {
    
    public static Mono<String> getCurrentUserId(ServerWebExchange exchange) {
        String userId = exchange.getAttribute("userId");
        return userId != null ? Mono.just(userId) : Mono.empty();
    }
    
    public static Mono<String> getCurrentUserRole(ServerWebExchange exchange) {
        String role = exchange.getAttribute("userRole");
        return role != null ? Mono.just(role) : Mono.empty();
    }
    
    public static String getUserIdFromRequest(org.springframework.web.reactive.function.server.ServerRequest request) {
        return (String) request.attribute("userId").orElse(null);
    }
    
    public static String getUserRoleFromRequest(org.springframework.web.reactive.function.server.ServerRequest request) {
        return (String) request.attribute("userRole").orElse(null);
    }
}