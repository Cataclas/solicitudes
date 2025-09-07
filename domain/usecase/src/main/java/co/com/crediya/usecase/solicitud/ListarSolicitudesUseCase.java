package co.com.crediya.usecase.solicitud;

import co.com.crediya.model.solicitud.SolicitudFilter;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ListarSolicitudesUseCase {
    
    private final SolicitudRepository solicitudRepository;
    
    public Flux<co.com.crediya.model.solicitud.Solicitud> listarSolicitudesParaRevision() {
        return solicitudRepository.findByEstadosParaRevision();
    }
    
    public Flux<co.com.crediya.model.solicitud.Solicitud> listarSolicitudesParaRevisionConFiltros(SolicitudFilter filter) {
        return solicitudRepository.findByEstadosParaRevisionWithFilters(filter);
    }
    
    public Mono<Long> contarSolicitudesParaRevisionConFiltros(SolicitudFilter filter) {
        return solicitudRepository.countByEstadosParaRevisionWithFilters(filter);
    }
}