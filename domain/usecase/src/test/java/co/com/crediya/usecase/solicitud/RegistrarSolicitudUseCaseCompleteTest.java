package co.com.crediya.usecase.solicitud;

import co.com.crediya.model.estado.Estado;
import co.com.crediya.model.estado.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.solicitud.exceptions.TipoPrestamoNoExisteException;
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
class RegistrarSolicitudUseCaseCompleteTest {

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
    void deberiaFallarCuandoTipoPrestamoNoExiste() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(999)
                .build();

        when(tipoPrestamoRepository.existsById(999)).thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(TipoPrestamoNoExisteException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoMontoEsCero() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(BigDecimal.ZERO)
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
    void deberiaFallarCuandoMontoEsNegativo() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("-1000"))
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
    void deberiaFallarCuandoPlazoEsCero() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(0)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoPlazoEsNegativo() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(-5)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoIdUsuarioEsVacio() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario("")
                .idTipoPrestamo(1)
                .build();

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoIdUsuarioEsSoloEspacios() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario("   ")
                .idTipoPrestamo(1)
                .build();

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaRegistrarSolicitudConTodosLosCampos() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("5000000"))
                .plazo(36)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(2)
                .build();

        Estado estado = Estado.builder()
                .idEstado(1)
                .nombre("PENDIENTE_REVISION")
                .build();

        when(tipoPrestamoRepository.existsById(2)).thenReturn(Mono.just(true));
        when(estadoRepository.findByNombre("PENDIENTE_REVISION")).thenReturn(Mono.just(estado));
        when(solicitudRepository.guardar(any(Solicitud.class))).thenReturn(Mono.just(solicitud));

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectNextMatches(result -> 
                    result.getMonto().equals(new BigDecimal("5000000")) &&
                    result.getPlazo().equals(36) &&
                    result.getIdUsuario().equals("550e8400-e29b-41d4-a716-446655440005") &&
                    result.getIdTipoPrestamo().equals(2))
                .verifyComplete();
    }

    @Test
    void deberiaFallarCuandoEstadoInicialNoExiste() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        when(tipoPrestamoRepository.existsById(1)).thenReturn(Mono.just(true));
        when(estadoRepository.findByNombre("PENDIENTE_REVISION")).thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(useCase.registrar(solicitud))
                .expectError()
                .verify();
    }
}