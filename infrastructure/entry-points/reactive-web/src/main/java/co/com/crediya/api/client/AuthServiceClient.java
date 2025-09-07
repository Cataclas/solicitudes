package co.com.crediya.api.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${auth.service.url}")
    private String authServiceUrl;
    
    @Value("${auth.service.service-secret}")
    private String serviceSecret;
    
    private String serviceToken;
    
    public Mono<UserInfo> getUserInfo(String userId) {
        return getServiceToken()
                .flatMap(token -> webClientBuilder.build()
                        .post()
                        .uri(authServiceUrl + "/api/v1/users/info")
                        .header("Authorization", "Bearer " + token)
                        .bodyValue(new UserInfoRequest(userId))
                        .retrieve()
                        .bodyToMono(UserInfo.class)
                        .doOnError(error -> log.error("Error obteniendo info de usuario {}: {}", userId, error.getMessage()))
                        .onErrorReturn(UserInfo.empty()));
    }
    
    public Mono<TokenValidationResponse> validateToken(String token) {
        return webClientBuilder.build()
                .post()
                .uri(authServiceUrl + "/api/v1/validate-token")
                .bodyValue(new TokenValidationRequest(token))
                .retrieve()
                .bodyToMono(TokenValidationResponse.class)
                .doOnError(error -> log.error("Error validando token: {}", error.getMessage()))
                .onErrorReturn(TokenValidationResponse.invalid());
    }
    
    private Mono<String> getServiceToken() {
        if (serviceToken != null) {
            return Mono.just(serviceToken);
        }
        
        return webClientBuilder.build()
                .post()
                .uri(authServiceUrl + "/api/v1/service-token")
                .bodyValue(new ServiceTokenRequest("solicitudes", serviceSecret))
                .retrieve()
                .bodyToMono(ServiceTokenResponse.class)
                .map(response -> {
                    serviceToken = response.getServiceToken();
                    log.debug("Token de servicio obtenido: {}", serviceToken != null ? "OK" : "NULL");
                    return serviceToken;
                })
                .filter(token -> token != null)
                .doOnError(error -> log.error("Error obteniendo token de servicio: {}", error.getMessage()));
    }
    
    public static class UserInfo {
        private String idUsuario;
        private String nombreCompleto;
        private String email;
        private String documentoIdentidad;
        private BigDecimal salarioBase;
        private LocalDate fechaNacimiento;
        private String direccion;
        private String rol;
        
        public static UserInfo empty() {
            UserInfo info = new UserInfo();
            info.nombreCompleto = "Usuario no encontrado";
            info.email = "N/A";
            return info;
        }
        
        // Getters y setters
        public String getIdUsuario() { return idUsuario; }
        public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
        
        public String getNombreCompleto() { return nombreCompleto; }
        public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getDocumentoIdentidad() { return documentoIdentidad; }
        public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
        
        public BigDecimal getSalarioBase() { return salarioBase; }
        public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }
        
        public LocalDate getFechaNacimiento() { return fechaNacimiento; }
        public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
        
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
    
    private static class ServiceTokenRequest {
        private String serviceName;
        private String serviceSecret;
        
        public ServiceTokenRequest(String serviceName, String serviceSecret) {
            this.serviceName = serviceName;
            this.serviceSecret = serviceSecret;
        }
        
        public String getServiceName() { return serviceName; }
        public String getServiceSecret() { return serviceSecret; }
    }
    
    private static class ServiceTokenResponse {
        private String serviceToken;
        private String service_token; // Campo alternativo
        
        public String getServiceToken() { 
            return serviceToken != null ? serviceToken : service_token; 
        }
        public void setServiceToken(String serviceToken) { this.serviceToken = serviceToken; }
        public void setService_token(String service_token) { this.service_token = service_token; }
    }
    
    private static class TokenValidationRequest {
        private String token;
        
        public TokenValidationRequest(String token) {
            this.token = token;
        }
        
        public String getToken() { return token; }
    }
    
    private static class UserInfoRequest {
        private String userId;
        
        public UserInfoRequest(String userId) {
            this.userId = userId;
        }
        
        public String getUserId() { return userId; }
    }
    
    public static class TokenValidationResponse {
        private boolean valid;
        private String userId;
        private String roleId;
        
        public static TokenValidationResponse invalid() {
            TokenValidationResponse response = new TokenValidationResponse();
            response.valid = false;
            return response;
        }
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
    }
}