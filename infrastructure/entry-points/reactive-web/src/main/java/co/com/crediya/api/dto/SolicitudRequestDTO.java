package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Schema(description = "Datos para registrar una nueva solicitud de préstamo")
public class SolicitudRequestDTO {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @JsonProperty("monto")
    @Schema(description = "Monto solicitado del préstamo", example = "5000000.00", required = true)
    private BigDecimal monto;

    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 1, message = "El plazo debe ser mayor a 0")
    @JsonProperty("plazo")
    @Schema(description = "Plazo en meses para el préstamo", example = "24", required = true)
    private Integer plazo;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    @JsonProperty("id_tipo_prestamo")
    @Schema(description = "ID del tipo de préstamo", example = "1", required = true)
    private Integer idTipoPrestamo;
    
    // Campo interno - no se expone en la API
    @Schema(hidden = true)
    private String idUsuario;
}