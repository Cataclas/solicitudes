package co.com.crediya.usecase.solicitud;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudFilter;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarSolicitudesUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;
    
    private ListarSolicitudesUseCase listarSolicitudesUseCase;
    
    @BeforeEach
    void setUp() {
        listarSolicitudesUseCase = new ListarSolicitudesUseCase(solicitudRepository);
    }
    
    @Test
    void deberiaListarSolicitudesParaRevision() {
        // Given
        Solicitud solicitud1 = Solicitud.builder()
                .idSolicitud("sol-1")
                .monto(new BigDecimal("1000000"))
                .build();
                
        Solicitud solicitud2 = Solicitud.builder()
                .idSolicitud("sol-2")
                .monto(new BigDecimal("2000000"))
                .build();
                
        when(solicitudRepository.findByEstadosParaRevision())
                .thenReturn(Flux.just(solicitud1, solicitud2));
        
        // When & Then
        StepVerifier.create(listarSolicitudesUseCase.listarSolicitudesParaRevision())
                .expectNext(solicitud1)
                .expectNext(solicitud2)
                .verifyComplete();
    }
    
    @Test
    void deberiaListarSolicitudesConFiltros() {
        // Given
        SolicitudFilter filter = SolicitudFilter.builder()
                .page(0)
                .size(10)
                .montoMinimo(new BigDecimal("500000"))
                .build();
                
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud("sol-1")
                .monto(new BigDecimal("1000000"))
                .build();
                
        when(solicitudRepository.findByEstadosParaRevisionWithFilters(any(SolicitudFilter.class)))
                .thenReturn(Flux.just(solicitud));
        
        // When & Then
        StepVerifier.create(listarSolicitudesUseCase.listarSolicitudesParaRevisionConFiltros(filter))
                .expectNext(solicitud)
                .verifyComplete();
    }
    
    @Test
    void deberiaContarSolicitudesConFiltros() {
        // Given
        SolicitudFilter filter = SolicitudFilter.builder()
                .page(0)
                .size(10)
                .build();
                
        when(solicitudRepository.countByEstadosParaRevisionWithFilters(any(SolicitudFilter.class)))
                .thenReturn(Mono.just(5L));
        
        // When & Then
        StepVerifier.create(listarSolicitudesUseCase.contarSolicitudesParaRevisionConFiltros(filter))
                .expectNext(5L)
                .verifyComplete();
    }
    
    @Test
    void deberiaRetornarListaVaciaCuandoNoHaySolicitudes() {
        // Given
        when(solicitudRepository.findByEstadosParaRevision())
                .thenReturn(Flux.empty());
        
        // When & Then
        StepVerifier.create(listarSolicitudesUseCase.listarSolicitudesParaRevision())
                .verifyComplete();
    }
    
    @Test
    void deberiaRetornarCeroCuandoNoHaySolicitudesParaContar() {
        // Given
        SolicitudFilter filter = SolicitudFilter.builder().build();
        
        when(solicitudRepository.countByEstadosParaRevisionWithFilters(any(SolicitudFilter.class)))
                .thenReturn(Mono.just(0L));
        
        // When & Then
        StepVerifier.create(listarSolicitudesUseCase.contarSolicitudesParaRevisionConFiltros(filter))
                .expectNext(0L)
                .verifyComplete();
    }
}