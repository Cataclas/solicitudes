package co.com.crediya.api;

import co.com.crediya.api.client.AuthServiceClient;
import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.PagedResponseDTO;
import co.com.crediya.api.dto.SolicitudFilterDTO;
import co.com.crediya.api.dto.SolicitudListResponseDTO;
import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.mapper.SolicitudMapper;
import co.com.crediya.model.estado.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.SolicitudFilter;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.solicitud.exceptions.TipoPrestamoNoExisteException;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.crediya.usecase.solicitud.ListarSolicitudesUseCase;
import co.com.crediya.usecase.solicitud.RegistrarSolicitudUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final SolicitudMapper solicitudMapper;
    private final EstadoRepository estadoRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final AuthServiceClient authServiceClient;

    public Mono<ServerResponse> registrarSolicitud(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando registro de solicitud", traceId);
        
        // Obtener información del usuario autenticado
        String userId = (String) serverRequest.attribute("userId").orElse(null);
        String userRole = (String) serverRequest.attribute("userRole").orElse(null);
        
        if (userId == null) {
            log.warn("[{}] Usuario no autenticado", traceId);
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .bodyValue(ErrorResponseDTO.of("Usuario no autenticado", "UNAUTHORIZED"));
        }
        
        log.debug("[{}] Procesando solicitud para usuario: {} con rol: {}", traceId, userId, userRole);
        
        return serverRequest.bodyToMono(SolicitudRequestDTO.class)
                .doOnNext(dto -> {
                    // Asignar el ID del usuario autenticado a la solicitud
                    dto.setIdUsuario(userId);
                    log.debug("[{}] Solicitud asignada al usuario: {}", traceId, userId);
                })
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
                .onErrorResume(RuntimeException.class, ex -> {
                    log.error("[{}] Error interno en registro de solicitud: {}", traceId, ex.getClass().getSimpleName(), ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of(
                                "Algo salió mal. Nuestro equipo técnico ha sido notificado y está trabajando para solucionarlo", 
                                "INTERNAL_ERROR", 
                                traceId));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error interno en registro de solicitud: {}", traceId, ex.getClass().getSimpleName(), ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of(
                                "Algo salió mal. Nuestro equipo técnico ha sido notificado y está trabajando para solucionarlo", 
                                "INTERNAL_ERROR", 
                                traceId));
                });
    }
    
    public Mono<ServerResponse> listarSolicitudes(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando listado de solicitudes para revisión", traceId);
        
        // Extraer parámetros de query
        var filterDTO = new SolicitudFilterDTO();
        filterDTO.setPage(serverRequest.queryParam("page").map(Integer::parseInt).orElse(0));
        filterDTO.setSize(serverRequest.queryParam("size").map(Integer::parseInt).orElse(10));
        filterDTO.setEstado(serverRequest.queryParam("estado").orElse(null));
        filterDTO.setTipoPrestamo(serverRequest.queryParam("tipoPrestamo").orElse(null));
        filterDTO.setMontoMinimo(serverRequest.queryParam("montoMinimo").map(BigDecimal::new).orElse(null));
        filterDTO.setMontoMaximo(serverRequest.queryParam("montoMaximo").map(BigDecimal::new).orElse(null));
        filterDTO.setEmail(serverRequest.queryParam("email").orElse(null));
        
        SolicitudFilter filter = SolicitudFilter.builder()
                .page(filterDTO.getPage())
                .size(filterDTO.getSize())
                .estado(filterDTO.getEstado())
                .tipoPrestamo(filterDTO.getTipoPrestamo())
                .montoMinimo(filterDTO.getMontoMinimo())
                .montoMaximo(filterDTO.getMontoMaximo())
                .email(filterDTO.getEmail())
                .build();
        
        return Mono.zip(
                listarSolicitudesUseCase.listarSolicitudesParaRevisionConFiltros(filter)
                        .flatMap(solicitud -> 
                            Mono.zip(
                                estadoRepository.findById(solicitud.getIdEstado()),
                                tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo()),
                                authServiceClient.getUserInfo(solicitud.getIdUsuario())
                            ).map(tuple -> {
                                var estado = tuple.getT1();
                                var tipoPrestamo = tuple.getT2();
                                var usuario = tuple.getT3();
                                
                                return SolicitudListResponseDTO.builder()
                                        .idSolicitud(solicitud.getIdSolicitud())
                                        .monto(solicitud.getMonto())
                                        .plazo(solicitud.getPlazo())
                                        .email(usuario.getEmail())
                                        .nombre(usuario.getNombreCompleto())
                                        .tipoPrestamo(tipoPrestamo.getNombre())
                                        .tasaInteres(tipoPrestamo.getTasaInteres())
                                        .estadoSolicitud(estado.getNombre())
                                        .salarioBase(usuario.getSalarioBase())
                                        .deudaTotalMensual(BigDecimal.ZERO)
                                        .fechaCreacion(solicitud.getCreatedAt())
                                        .estado(estado)
                                        .tipo(tipoPrestamo)
                                        .usuario(usuario)
                                        .build();
                            }))
                        .filter(dto -> {
                            // Aplicar filtros adicionales en el Handler
                            if (filter.getEstado() != null && !dto.getEstado().getNombre().equals(filter.getEstado())) {
                                return false;
                            }
                            if (filter.getTipoPrestamo() != null && !dto.getTipo().getNombre().equals(filter.getTipoPrestamo())) {
                                return false;
                            }
                            if (filter.getEmail() != null && !dto.getUsuario().getEmail().toLowerCase().contains(filter.getEmail().toLowerCase())) {
                                return false;
                            }
                            return true;
                        })
                        .collectList(),
                listarSolicitudesUseCase.contarSolicitudesParaRevisionConFiltros(filter)
        ).flatMap(tuple -> {
            var solicitudes = tuple.getT1();
            var totalElements = tuple.getT2();
            var totalPages = (int) Math.ceil((double) totalElements / filterDTO.getSize());
            
            var pagedResponse = PagedResponseDTO.<SolicitudListResponseDTO>builder()
                    .content(solicitudes)
                    .page(filterDTO.getPage())
                    .size(filterDTO.getSize())
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .first(filterDTO.getPage() == 0)
                    .last(filterDTO.getPage() >= totalPages - 1)
                    .build();
            
            log.info("[{}] Listado paginado obtenido - {} solicitudes, página {}/{}", 
                    traceId, solicitudes.size(), filterDTO.getPage() + 1, totalPages);
            return ServerResponse.ok().bodyValue(pagedResponse);
        })
        .onErrorResume(Exception.class, ex -> {
            log.error("[{}] Error obteniendo listado de solicitudes: {}", traceId, ex.getMessage());
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue(ErrorResponseDTO.of(
                        "Error interno del servidor", 
                        "ERROR_INTERNO", 
                        traceId));
        });
    }
}