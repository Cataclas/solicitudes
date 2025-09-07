package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Solicitud en listado para revisión")
public class SolicitudListResponseDTO {
    
    @Schema(description = "ID de la solicitud", example = "550e8400-e29b-41d4-a716-446655440000")
    private String idSolicitud;
    
    @Schema(description = "Monto solicitado", example = "5000000.00")
    private BigDecimal monto;
    
    @Schema(description = "Plazo en meses", example = "24")
    private Integer plazo;
    
    @Schema(description = "Email del solicitante", example = "cliente@email.com")
    private String email;
    
    @Schema(description = "Nombre completo del solicitante", example = "María Cliente")
    private String nombre;
    
    @Schema(description = "Tipo de préstamo", example = "PERSONAL")
    private String tipoPrestamo;
    
    @Schema(description = "Tasa de interés", example = "0.0250")
    private BigDecimal tasaInteres;
    
    @Schema(description = "Estado de la solicitud", example = "PENDIENTE_REVISION")
    private String estadoSolicitud;
    
    @Schema(description = "Salario base del solicitante", example = "2500000.00")
    private BigDecimal salarioBase;
    
    @Schema(description = "Deuda total mensual", example = "800000.00")
    private BigDecimal deudaTotalMensual;
    
    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;
    
    // Campos auxiliares para filtros (no se serializan en JSON)
    @Schema(hidden = true)
    private co.com.crediya.model.estado.Estado estado;
    
    @Schema(hidden = true)
    private co.com.crediya.model.tipoprestamo.TipoPrestamo tipo;
    
    @Schema(hidden = true)
    private co.com.crediya.api.client.AuthServiceClient.UserInfo usuario;
}