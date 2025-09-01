package co.com.crediya.usecase.solicitud;

import co.com.crediya.model.estado.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.solicitud.exceptions.TipoPrestamoNoExisteException;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {
    
    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final String ESTADO_INICIAL = "PENDIENTE_REVISION";

    public Mono<Solicitud> registrar(Solicitud solicitud) {
        return validarDatos(solicitud)
                .then(validarTipoPrestamoExiste(solicitud.getIdTipoPrestamo()))
                .then(obtenerEstadoInicial())
                .flatMap(estado -> guardarSolicitud(solicitud, estado.getIdEstado()));
    }

    private Mono<Void> validarDatos(Solicitud solicitud) {
        List<String> errores = new ArrayList<>();
        
        if (solicitud == null) {
            errores.add("Los datos de la solicitud son requeridos");
            return Mono.error(new DatosInvalidosException(errores));
        }
        
        // Validar campos obligatorios
        if (solicitud.getMonto() == null) {
            errores.add("El monto es obligatorio");
        }
        if (solicitud.getPlazo() == null) {
            errores.add("El plazo es obligatorio");
        }
        if (esNuloOVacio(solicitud.getEmail())) {
            errores.add("El email es obligatorio");
        }
        if (esNuloOVacio(solicitud.getDocumentoIdentidad())) {
            errores.add("El documento de identidad es obligatorio");
        }
        if (solicitud.getIdTipoPrestamo() == null) {
            errores.add("El tipo de préstamo es obligatorio");
        }
        
        // Validar formatos solo si los campos no están vacíos
        if (!esNuloOVacio(solicitud.getEmail()) && !EMAIL_PATTERN.matcher(solicitud.getEmail()).matches()) {
            errores.add("El formato del email no es válido");
        }
        
        if (solicitud.getMonto() != null && solicitud.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El monto debe ser mayor a 0");
        }
        
        if (solicitud.getPlazo() != null && solicitud.getPlazo() <= 0) {
            errores.add("El plazo debe ser mayor a 0");
        }
        
        if (!errores.isEmpty()) {
            return Mono.error(new DatosInvalidosException(errores));
        }
        
        return Mono.empty();
    }

    private Mono<Void> validarTipoPrestamoExiste(Integer idTipoPrestamo) {
        return tipoPrestamoRepository.existsById(idTipoPrestamo)
                .flatMap(existe -> existe ? 
                    Mono.empty() : 
                    Mono.error(new TipoPrestamoNoExisteException("El tipo de préstamo seleccionado no existe")));
    }

    private Mono<co.com.crediya.model.estado.Estado> obtenerEstadoInicial() {
        return estadoRepository.findByNombre(ESTADO_INICIAL);
    }

    private Mono<Solicitud> guardarSolicitud(Solicitud solicitud, Integer idEstado) {
        LocalDateTime now = LocalDateTime.now();
        Solicitud solicitudConId = solicitud.toBuilder()
                .idSolicitud(UUID.randomUUID().toString())
                .idEstado(idEstado)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .active(true)
                .build();
        return solicitudRepository.guardar(solicitudConId);
    }

    private boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}