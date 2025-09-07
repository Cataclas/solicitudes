package co.com.crediya.model.solicitud;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SolicitudFilter {
    private Integer page;
    private Integer size;
    private String estado;
    private String tipoPrestamo;
    private BigDecimal montoMinimo;
    private BigDecimal montoMaximo;
    private String email;
}