package co.com.crediya.r2dbc.solicitud;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("solicitud")
public class SolicitudEntity {
    @Id
    @Column("id_solicitud")
    private String idSolicitud;
    
    @Column("monto")
    private BigDecimal monto;
    
    @Column("plazo")
    private Integer plazo;
    
    @Column("email")
    private String email;
    
    @Column("documento_identidad")
    private String documentoIdentidad;
    
    @Column("id_estado")
    private Integer idEstado;
    
    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;
    
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