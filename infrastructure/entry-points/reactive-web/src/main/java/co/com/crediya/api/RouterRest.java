package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.dto.SolicitudResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "registrarSolicitud",
                summary = "Registrar nueva solicitud de préstamo",
                description = "Registra una nueva solicitud de préstamo con validaciones de negocio",
                tags = {"Solicitudes"},
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
        return route(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), 
                     handler::registrarSolicitud);
    }
}