package co.com.crediya.model.solicitud.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoPrestamoNoExisteExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        // Given
        String mensaje = "El tipo de préstamo con ID 999 no existe";

        // When
        TipoPrestamoNoExisteException exception = new TipoPrestamoNoExisteException(mensaje);

        // Then
        assertEquals(mensaje, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void deberiaSerRuntimeException() {
        // Given
        TipoPrestamoNoExisteException exception = new TipoPrestamoNoExisteException("Test");

        // When & Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void deberiaManejarMensajeNulo() {
        // Given
        String mensajeNulo = null;

        // When
        TipoPrestamoNoExisteException exception = new TipoPrestamoNoExisteException(mensajeNulo);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    void deberiaManejarMensajeVacio() {
        // Given
        String mensajeVacio = "";

        // When
        TipoPrestamoNoExisteException exception = new TipoPrestamoNoExisteException(mensajeVacio);

        // Then
        assertEquals("", exception.getMessage());
    }
}