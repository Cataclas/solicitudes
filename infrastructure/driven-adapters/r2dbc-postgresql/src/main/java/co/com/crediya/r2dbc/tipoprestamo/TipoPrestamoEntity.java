package co.com.crediya.r2dbc.tipoprestamo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("tipo_prestamo")
public class TipoPrestamoEntity {
    @Id
    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;
    
    @Column("nombre")
    private String nombre;
    
    @Column("monto_minimo")
    private BigDecimal montoMinimo;
    
    @Column("monto_maximo")
    private BigDecimal montoMaximo;
    
    @Column("tasa_interes")
    private BigDecimal tasaInteres;
    
    @Column("validacion_automatica")
    private Boolean validacionAutomatica;
    
    // Campos de auditoría
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
    
    @Column("created_by")
    private String createdBy;
    
    @Column("updated_by")
    private String updatedBy;
    
    @Column("active")
    private Boolean active;
}