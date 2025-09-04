package co.com.crediya.api.config;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.solicitud.exceptions.TipoPrestamoNoExisteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import jakarta.validation.ConstraintViolationException;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatosInvalidos(DatosInvalidosException ex) {
        String traceId = generateTraceId();
        log.warn("[{}] Errores de validación de negocio: {}", traceId, ex.getErrores().size());
        
        return ResponseEntity.badRequest()
                .body(ErrorResponseDTO.builder()
                        .mensaje("Los datos proporcionados no son válidos")
                        .errores(ex.getErrores())
                        .codigoError("VALIDATION_ERROR")
                        .traceId(traceId)
                        .timestamp(java.time.LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(TipoPrestamoNoExisteException.class)
    public ResponseEntity<ErrorResponseDTO> handleTipoPrestamoNoExiste(TipoPrestamoNoExisteException ex) {
        String traceId = generateTraceId();
        log.warn("[{}] Tipo de préstamo no existe", traceId);
        
        return ResponseEntity.badRequest()
                .body(ErrorResponseDTO.of("El tipo de préstamo especificado no existe", "LOAN_TYPE_NOT_FOUND", traceId));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(WebExchangeBindException ex) {
        String traceId = generateTraceId();
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Error de validación en los datos de entrada");
        
        log.warn("[{}] Error de validación HTTP: {}", traceId, mensaje);
        return ResponseEntity.badRequest()
                .body(ErrorResponseDTO.of(mensaje, "VALIDATION_ERROR", traceId));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        String traceId = generateTraceId();
        String mensaje = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Error de validación");
        
        log.warn("[{}] Error de validación de constraint: {}", traceId, mensaje);
        return ResponseEntity.badRequest()
                .body(ErrorResponseDTO.of(mensaje, "VALIDATION_ERROR", traceId));
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}