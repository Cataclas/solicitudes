package co.com.crediya.model.solicitud;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Solicitud {
    private String idSolicitud;
    private String idUsuario;
    private BigDecimal monto;
    private Integer plazo;
    private Integer idEstado;
    private Integer idTipoPrestamo;
    // Campos de auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Boolean active;
}