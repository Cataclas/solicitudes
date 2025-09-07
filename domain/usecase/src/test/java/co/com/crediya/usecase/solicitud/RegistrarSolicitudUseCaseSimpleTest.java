package co.com.crediya.usecase.solicitud;

import co.com.crediya.model.estado.Estado;
import co.com.crediya.model.estado.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarSolicitudUseCaseSimpleTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private EstadoRepository estadoRepository;

    private RegistrarSolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository, estadoRepository);
    }

    @Test
    void deberiaRegistrarSolicitudExitosamente() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        when(tipoPrestamoRepository.existsById(1)).thenReturn(Mono.just(true));
        when(estadoRepository.findByNombre("PENDIENTE_REVISION")).thenReturn(Mono.just(
            Estado.builder().idEstado(1).nombre("PENDIENTE_REVISION").build()
        ));
        when(solicitudRepository.guardar(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void deberiaFallarCuandoSolicitudEsNula() {
        // When & Then
        StepVerifier.create(useCase.registrar(null))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoMontoEsNulo() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(null)
                .plazo(12)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoIdUsuarioEsNulo() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario(null)
                .idTipoPrestamo(1)
                .build();

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
}