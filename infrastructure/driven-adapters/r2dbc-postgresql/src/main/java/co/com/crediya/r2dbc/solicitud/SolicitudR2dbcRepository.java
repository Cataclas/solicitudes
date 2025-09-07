package co.com.crediya.r2dbc.solicitud;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SolicitudR2dbcRepository extends ReactiveCrudRepository<SolicitudEntity, String> {
    
    @Query("INSERT INTO solicitud (id_solicitud, id_usuario, monto, plazo, id_estado, id_tipo_prestamo, created_at, updated_at, created_by, updated_by, active) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)")
    Mono<Void> insertSolicitud(String idSolicitud, String idUsuario, BigDecimal monto, Integer plazo, Integer idEstado, Integer idTipoPrestamo, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Boolean active);
    
    @Query("SELECT s.* FROM solicitud s INNER JOIN estados e ON s.id_estado = e.id_estado WHERE e.nombre IN ('PENDIENTE_REVISION', 'RECHAZADA', 'REVISION_MANUAL') AND s.active = true ORDER BY s.created_at DESC")
    Flux<SolicitudEntity> findByEstadosParaRevision();
}