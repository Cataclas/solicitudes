package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Solicitud> guardar(Solicitud solicitud);
    Flux<Solicitud> findByEstadosParaRevision();
    Flux<Solicitud> findByEstadosParaRevisionWithFilters(SolicitudFilter filter);
    Mono<Long> countByEstadosParaRevisionWithFilters(SolicitudFilter filter);
}