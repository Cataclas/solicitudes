package co.com.crediya.r2dbc.solicitud;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SolicitudR2dbcRepository extends ReactiveCrudRepository<SolicitudEntity, String> {
    
    @Query("INSERT INTO solicitud (id_solicitud, monto, plazo, email, documento_identidad, id_estado, id_tipo_prestamo, created_at, updated_at, created_by, updated_by, active) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)")
    Mono<Void> insertSolicitud(String idSolicitud, BigDecimal monto, Integer plazo, String email, String documentoIdentidad, Integer idEstado, Integer idTipoPrestamo, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Boolean active);
}