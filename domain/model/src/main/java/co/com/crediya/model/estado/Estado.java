package co.com.crediya.model.estado;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Estado {
    private Integer idEstado;
    private String nombre;
    private String descripcion;
    // Campos de auditoría
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Boolean active;
}