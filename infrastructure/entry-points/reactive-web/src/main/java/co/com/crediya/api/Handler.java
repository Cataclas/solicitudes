package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.mapper.SolicitudMapper;
import co.com.crediya.model.estado.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.solicitud.exceptions.TipoPrestamoNoExisteException;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.crediya.usecase.solicitud.RegistrarSolicitudUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final SolicitudMapper solicitudMapper;
    private final EstadoRepository estadoRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Mono<ServerResponse> registrarSolicitud(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando registro de solicitud", traceId);
        
        return serverRequest.bodyToMono(SolicitudRequestDTO.class)
                .doOnNext(dto -> log.debug("[{}] Procesando solicitud para documento: {}", traceId, 
                    dto.getDocumentoIdentidad() != null ? dto.getDocumentoIdentidad().substring(0, 3) + "***" : "null"))
                .map(solicitudMapper::toDomain)
                .flatMap(registrarSolicitudUseCase::registrar)
                .flatMap(solicitud -> 
                    Mono.zip(
                        estadoRepository.findByNombre("PENDIENTE_REVISION"),
                        tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                    ).map(tuple -> solicitudMapper.toResponse(solicitud, tuple.getT1(), tuple.getT2()))
                )
                .flatMap(response -> {
                    log.info("[{}] Solicitud registrada exitosamente - ID: {}", traceId, response.getIdSolicitud());
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                })
                .onErrorResume(DatosInvalidosException.class, ex -> {
                    log.warn("[{}] Errores de validación: {}", traceId, ex.getErrores().size());
                    log.debug("[{}] Detalles de validación: {}", traceId, ex.getErrores());
                    return ServerResponse.badRequest()
                            .bodyValue(ErrorResponseDTO.of(ex.getErrores(), "INVALID_DATA"));
                })
                .onErrorResume(TipoPrestamoNoExisteException.class, ex -> {
                    log.warn("[{}] Tipo de préstamo no existe", traceId);
                    return ServerResponse.badRequest()
                            .bodyValue(ErrorResponseDTO.of(ex.getMessage(), "LOAN_TYPE_NOT_FOUND"));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error interno en registro de solicitud: {}", traceId, ex.getClass().getSimpleName(), ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of("Error interno del servidor", "INTERNAL_ERROR"));
                });
    }
}