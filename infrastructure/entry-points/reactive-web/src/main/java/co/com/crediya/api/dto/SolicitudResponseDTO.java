package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Respuesta del registro de solicitud")
public class SolicitudResponseDTO {

    @Schema(description = "ID único de la solicitud", example = "550e8400-e29b-41d4-a716-446655440000")
    private String idSolicitud;

    @Schema(description = "Monto solicitado", example = "5000000.00")
    private BigDecimal monto;

    @Schema(description = "Plazo en meses", example = "24")
    private Integer plazo;

    @Schema(description = "Estado de la solicitud")
    private EstadoDTO estado;

    @Schema(description = "Tipo de préstamo")
    private TipoPrestamoDTO tipoPrestamo;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Estado activo", example = "true")
    private Boolean active;
}