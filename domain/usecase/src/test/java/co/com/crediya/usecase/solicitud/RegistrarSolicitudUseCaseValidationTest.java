package co.com.crediya.usecase.solicitud;

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
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class RegistrarSolicitudUseCaseValidationTest {

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
    void deberiaFallarCuandoSolicitudEsNula() {
        StepVerifier.create(useCase.registrar(null))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoMontoEsNulo() {
        Solicitud solicitud = Solicitud.builder()
                .monto(null)
                .plazo(12)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoIdUsuarioEsNulo() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .idUsuario(null)
                .idTipoPrestamo(1)
                .build();

        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }

    @Test
    void deberiaFallarCuandoPlazoEsNulo() {
        Solicitud solicitud = Solicitud.builder()
                .monto(new BigDecimal("1000000"))
                .plazo(null)
                .idUsuario("550e8400-e29b-41d4-a716-446655440005")
                .idTipoPrestamo(1)
                .build();

        StepVerifier.create(useCase.registrar(solicitud))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
}