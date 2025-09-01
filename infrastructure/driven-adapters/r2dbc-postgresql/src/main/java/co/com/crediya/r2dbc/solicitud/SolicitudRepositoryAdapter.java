package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final SolicitudR2dbcRepository r2dbcRepository;

    @Override
    @Transactional
    public Mono<Solicitud> guardar(Solicitud solicitud) {
        log.debug("Guardando solicitud en BD - documento: {}", 
            solicitud.getDocumentoIdentidad().substring(0, 3) + "***");
        
        return r2dbcRepository.insertSolicitud(
                solicitud.getIdSolicitud(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getEmail(),
                solicitud.getDocumentoIdentidad(),
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
}