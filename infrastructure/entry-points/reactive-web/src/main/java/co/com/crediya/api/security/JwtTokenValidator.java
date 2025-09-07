package co.com.crediya.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtTokenValidator {
    
    private final SecretKey secretKey;
    
    public JwtTokenValidator(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public Mono<UserInfo> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
                    
            String userId = claims.getSubject();
            String roleId = claims.get("role", String.class);
            
            return Mono.just(new UserInfo(userId, roleId));
        } catch (Exception e) {
            log.debug("Token inválido: {}", e.getMessage());
            return Mono.error(new RuntimeException("Token inválido"));
        }
    }
    
    public static class UserInfo {
        private final String userId;
        private final String roleId;
        
        public UserInfo(String userId, String roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }
        
        public String getUserId() { return userId; }
        public String getRoleId() { return roleId; }
        
        public boolean isAdmin() {
            return "550e8400-e29b-41d4-a716-446655440001".equals(roleId);
        }
        
        public boolean isAsesor() {
            return "550e8400-e29b-41d4-a716-446655440003".equals(roleId);
        }
        
        public boolean isCliente() {
            return "550e8400-e29b-41d4-a716-446655440006".equals(roleId);
        }
    }
}