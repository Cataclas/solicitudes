package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Filtros para listado de solicitudes")
public class SolicitudFilterDTO {
    
    @Schema(description = "Página (inicia en 0)", example = "0", defaultValue = "0")
    private Integer page = 0;
    
    @Schema(description = "Tamaño de página", example = "10", defaultValue = "10")
    private Integer size = 10;
    
    @Schema(description = "Estado de solicitud", example = "PENDIENTE_REVISION")
    private String estado;
    
    @Schema(description = "Tipo de préstamo", example = "PERSONAL")
    private String tipoPrestamo;
    
    @Schema(description = "Monto mínimo", example = "1000000")
    private BigDecimal montoMinimo;
    
    @Schema(description = "Monto máximo", example = "5000000")
    private BigDecimal montoMaximo;
    
    @Schema(description = "Email del solicitante", example = "cliente@email.com")
    private String email;
}