package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Estado de la solicitud")
public class EstadoDTO {
    
    @Schema(description = "ID del estado", example = "1")
    private Integer id;
    
    @Schema(description = "Nombre del estado", example = "PENDIENTE_REVISION")
    private String nombre;
    
    @Schema(description = "Descripción del estado", example = "Solicitud pendiente de revisión")
    private String descripcion;
}