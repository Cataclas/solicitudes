package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.SolicitudListResponseDTO;
import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.dto.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Solicitudes", description = "API para gestión de solicitudes de préstamo")
public class RouterRest {
    
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/solicitud",
            method = RequestMethod.GET,
            operation = @Operation(
                operationId = "listarSolicitudes",
                summary = "Listar solicitudes para revisión",
                description = "Obtiene listado paginado y filtrable de solicitudes que requieren revisión manual. REQUIERE TOKEN JWT. Solo usuarios con rol ASESOR pueden usar este endpoint.",
                tags = {"Solicitudes"},
                security = @SecurityRequirement(name = "bearerAuth"),
                parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "page", description = "Número de página (inicia en 0)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
                    @io.swagger.v3.oas.annotations.Parameter(name = "size", description = "Tamaño de página", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
                    @io.swagger.v3.oas.annotations.Parameter(name = "estado", description = "Filtrar por estado", example = "PENDIENTE_REVISION", schema = @Schema(type = "string")),
                    @io.swagger.v3.oas.annotations.Parameter(name = "tipoPrestamo", description = "Filtrar por tipo de préstamo", example = "PERSONAL", schema = @Schema(type = "string")),
                    @io.swagger.v3.oas.annotations.Parameter(name = "montoMinimo", description = "Monto mínimo", example = "1000000", schema = @Schema(type = "number")),
                    @io.swagger.v3.oas.annotations.Parameter(name = "montoMaximo", description = "Monto máximo", example = "5000000", schema = @Schema(type = "number")),
                    @io.swagger.v3.oas.annotations.Parameter(name = "email", description = "Email del solicitante", example = "cliente@email.com", schema = @Schema(type = "string"))
                },
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Listado obtenido exitosamente",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudListResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Token JWT requerido",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Sin permisos - Solo usuarios con rol ASESOR pueden listar solicitudes",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/solicitud",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "registrarSolicitud",
                summary = "Registrar nueva solicitud de préstamo",
                description = "Registra una nueva solicitud de préstamo con validaciones de negocio. REQUIERE TOKEN JWT. Solo usuarios con rol CLIENTE o SOLICITANTE pueden usar este endpoint.",
                tags = {"Solicitudes"},
                security = @SecurityRequirement(name = "bearerAuth"),
                requestBody = @RequestBody(
                    description = "Datos de la solicitud a registrar",
                    required = true,
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SolicitudRequestDTO.class)
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Solicitud registrada exitosamente",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos o tipo de préstamo no existe",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Token JWT requerido",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Sin permisos - Solo usuarios con rol CLIENTE o SOLICITANTE pueden crear solicitudes",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno del servidor",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                        )
                    )
                }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), 
                     handler::listarSolicitudes)
                .andRoute(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), 
                         handler::registrarSolicitud);
    }
}