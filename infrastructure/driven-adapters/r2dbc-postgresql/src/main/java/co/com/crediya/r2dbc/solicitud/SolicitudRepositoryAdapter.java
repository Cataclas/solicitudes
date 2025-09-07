package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudFilter;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final SolicitudR2dbcRepository r2dbcRepository;

    @Override
    @Transactional
    public Mono<Solicitud> guardar(Solicitud solicitud) {
        log.debug("Guardando solicitud en BD - usuario: {}", 
            solicitud.getIdUsuario());
        
        return r2dbcRepository.insertSolicitud(
                solicitud.getIdSolicitud(),
                solicitud.getIdUsuario(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getIdEstado(),
                solicitud.getIdTipoPrestamo(),
                solicitud.getCreatedAt(),
                solicitud.getUpdatedAt(),
                solicitud.getCreatedBy(),
                solicitud.getUpdatedBy(),
                solicitud.getActive()
        ).then(Mono.just(solicitud))
        .doOnSuccess(solicitudGuardada -> log.debug("Solicitud persistida exitosamente - ID: {}", solicitudGuardada.getIdSolicitud()));
    }
    
    @Override
    public Flux<Solicitud> findByEstadosParaRevision() {
        log.debug("Consultando solicitudes para revisión");
        return r2dbcRepository.findByEstadosParaRevision()
                .map(entity -> Solicitud.builder()
                        .idSolicitud(entity.getIdSolicitud())
                        .idUsuario(entity.getIdUsuario())
                        .monto(entity.getMonto())
                        .plazo(entity.getPlazo())
                        .idEstado(entity.getIdEstado())
                        .idTipoPrestamo(entity.getIdTipoPrestamo())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .createdBy(entity.getCreatedBy())
                        .updatedBy(entity.getUpdatedBy())
                        .active(entity.getActive())
                        .build())
                .doOnNext(solicitud -> log.debug("Solicitud encontrada: {}", solicitud.getIdSolicitud()));
    }
    
    @Override
    public Flux<Solicitud> findByEstadosParaRevisionWithFilters(SolicitudFilter filter) {
        return findByEstadosParaRevision()
                .filter(solicitud -> {
                    // Filtro por monto mínimo
                    if (filter.getMontoMinimo() != null && 
                        solicitud.getMonto().compareTo(filter.getMontoMinimo()) < 0) {
                        return false;
                    }
                    // Filtro por monto máximo
                    if (filter.getMontoMaximo() != null && 
                        solicitud.getMonto().compareTo(filter.getMontoMaximo()) > 0) {
                        return false;
                    }
                    return true;
                })
                .skip(filter.getPage() * filter.getSize())
                .take(filter.getSize());
    }
    
    @Override
    public Mono<Long> countByEstadosParaRevisionWithFilters(SolicitudFilter filter) {
        return findByEstadosParaRevision()
                .filter(solicitud -> {
                    // Aplicar los mismos filtros básicos
                    if (filter.getMontoMinimo() != null && 
                        solicitud.getMonto().compareTo(filter.getMontoMinimo()) < 0) {
                        return false;
                    }
                    if (filter.getMontoMaximo() != null && 
                        solicitud.getMonto().compareTo(filter.getMontoMaximo()) > 0) {
                        return false;
                    }
                    return true;
                })
                .count();
    }
}