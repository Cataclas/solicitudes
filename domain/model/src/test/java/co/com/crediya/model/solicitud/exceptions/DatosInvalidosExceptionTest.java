package co.com.crediya.model.solicitud.exceptions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatosInvalidosExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensajeUnico() {
        // Given
        String mensaje = "El monto es obligatorio";

        // When
        DatosInvalidosException exception = new DatosInvalidosException(mensaje);

        // Then
        assertEquals(mensaje, exception.getMessage());
        assertEquals(1, exception.getErrores().size());
        assertEquals(mensaje, exception.getErrores().get(0));
    }

    @Test
    void deberiaCrearExcepcionConListaDeErrores() {
        // Given
        List<String> errores = Arrays.asList(
            "El monto es obligatorio",
            "El plazo debe ser mayor a 0",
            "El email no es válido"
        );

        // When
        DatosInvalidosException exception = new DatosInvalidosException(errores);

        // Then
        assertEquals("El monto es obligatorio, El plazo debe ser mayor a 0, El email no es válido", exception.getMessage());
        assertEquals(3, exception.getErrores().size());
        assertEquals(errores, exception.getErrores());
    }

    @Test
    void deberiaRetornarListaInmutableDeErrores() {
        // Given
        List<String> errores = Arrays.asList("Error 1", "Error 2");
        DatosInvalidosException exception = new DatosInvalidosException(errores);

        // When
        List<String> erroresObtenidos = exception.getErrores();

        // Then
        assertNotNull(erroresObtenidos);
        assertEquals(2, erroresObtenidos.size());
        
        // Verificar que es inmutable
        assertThrows(UnsupportedOperationException.class, () -> 
            erroresObtenidos.add("Nuevo error"));
    }

    @Test
    void deberiaManejarListaVacia() {
        // Given
        List<String> erroresVacios = Arrays.asList();

        // When
        DatosInvalidosException exception = new DatosInvalidosException(erroresVacios);

        // Then
        assertEquals("", exception.getMessage());
        assertTrue(exception.getErrores().isEmpty());
    }
}