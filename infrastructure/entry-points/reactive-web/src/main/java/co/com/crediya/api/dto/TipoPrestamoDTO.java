package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Tipo de préstamo")
public class TipoPrestamoDTO {
    
    @Schema(description = "ID del tipo de préstamo", example = "1")
    private Integer id;
    
    @Schema(description = "Nombre del tipo de préstamo", example = "PERSONAL")
    private String nombre;
    
    @Schema(description = "Monto mínimo", example = "500000.00")
    private BigDecimal montoMinimo;
    
    @Schema(description = "Monto máximo", example = "5000000.00")
    private BigDecimal montoMaximo;
    
    @Schema(description = "Tasa de interés", example = "0.0250")
    private BigDecimal tasaInteres;
}