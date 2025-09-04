package co.com.crediya.model.solicitud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void deberiaCrearSolicitudConBuilder() {
        // Given
        String idSolicitud = "SOL-001";
        BigDecimal monto = new BigDecimal("1000000");
        Integer plazo = 12;
        String email = "test@email.com";
        String documento = "12345678";
        Integer idEstado = 1;
        Integer idTipoPrestamo = 1;
        LocalDateTime now = LocalDateTime.now();

        // When
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(idSolicitud)
                .monto(monto)
                .plazo(plazo)
                .email(email)
                .documentoIdentidad(documento)
                .idEstado(idEstado)
                .idTipoPrestamo(idTipoPrestamo)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .active(true)
                .build();

        // Then
        assertNotNull(solicitud);
        assertEquals(idSolicitud, solicitud.getIdSolicitud());
        assertEquals(monto, solicitud.getMonto());
        assertEquals(plazo, solicitud.getPlazo());
        assertEquals(email, solicitud.getEmail());
        assertEquals(documento, solicitud.getDocumentoIdentidad());
        assertEquals(idEstado, solicitud.getIdEstado());
        assertEquals(idTipoPrestamo, solicitud.getIdTipoPrestamo());
        assertEquals(now, solicitud.getCreatedAt());
        assertEquals(now, solicitud.getUpdatedAt());
        assertEquals("SYSTEM", solicitud.getCreatedBy());
        assertEquals("SYSTEM", solicitud.getUpdatedBy());
        assertTrue(solicitud.getActive());
    }

    @Test
    void deberiaModificarSolicitudConToBuilder() {
        // Given
        Solicitud solicitudOriginal = Solicitud.builder()
                .idSolicitud("SOL-001")
                .monto(new BigDecimal("1000000"))
                .plazo(12)
                .email("test@email.com")
                .documentoIdentidad("12345678")
                .idEstado(1)
                .idTipoPrestamo(1)
                .active(true)
                .build();

        // When
        Solicitud solicitudModificada = solicitudOriginal.toBuilder()
                .idEstado(2)
                .updatedAt(LocalDateTime.now())
                .updatedBy("USER")
                .build();

        // Then
        assertEquals(solicitudOriginal.getIdSolicitud(), solicitudModificada.getIdSolicitud());
        assertEquals(solicitudOriginal.getMonto(), solicitudModificada.getMonto());
        assertEquals(2, solicitudModificada.getIdEstado());
        assertEquals("USER", solicitudModificada.getUpdatedBy());
        assertNotEquals(solicitudOriginal.getIdEstado(), solicitudModificada.getIdEstado());
    }

    @Test
    void deberiaManejarValoresNulos() {
        // When
        Solicitud solicitud = Solicitud.builder().build();

        // Then
        assertNotNull(solicitud);
        assertNull(solicitud.getIdSolicitud());
        assertNull(solicitud.getMonto());
        assertNull(solicitud.getPlazo());
        assertNull(solicitud.getEmail());
        assertNull(solicitud.getDocumentoIdentidad());
        assertNull(solicitud.getIdEstado());
        assertNull(solicitud.getIdTipoPrestamo());
    }

    @Test
    void deberiaValidarEqualsYHashCode() {
        // Given
        Solicitud solicitud1 = Solicitud.builder()
                .idSolicitud("SOL-001")
                .monto(new BigDecimal("1000000"))
                .email("test@email.com")
                .build();

        Solicitud solicitud2 = Solicitud.builder()
                .idSolicitud("SOL-001")
                .monto(new BigDecimal("1000000"))
                .email("test@email.com")
                .build();

        Solicitud solicitud3 = Solicitud.builder()
                .idSolicitud("SOL-002")
                .monto(new BigDecimal("2000000"))
                .email("otro@email.com")
                .build();

        // Then
        assertEquals(solicitud1, solicitud2);
        assertNotEquals(solicitud1, solicitud3);
        assertEquals(solicitud1.hashCode(), solicitud2.hashCode());
        assertNotEquals(solicitud1.hashCode(), solicitud3.hashCode());
        assertNotEquals(solicitud1, null);
        assertNotEquals(solicitud1, "string");
    }

    @Test
    void deberiaValidarToString() {
        // Given
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud("SOL-001")
                .monto(new BigDecimal("1000000"))
                .email("test@email.com")
                .build();

        // When
        String toString = solicitud.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("SOL-001"));
        assertTrue(toString.contains("1000000"));
        assertTrue(toString.contains("test@email.com"));
    }

    @Test
    void deberiaValidarCamposCompletos() {
        // Given
        String idSolicitud = "SOL-TEST";
        BigDecimal monto = new BigDecimal("500000");
        Integer plazo = 24;
        String email = "setter@test.com";
        String documento = "87654321";
        Integer idEstado = 3;
        Integer idTipoPrestamo = 2;
        LocalDateTime now = LocalDateTime.now();
        Boolean active = false;

        // When
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(idSolicitud)
                .monto(monto)
                .plazo(plazo)
                .email(email)
                .documentoIdentidad(documento)
                .idEstado(idEstado)
                .idTipoPrestamo(idTipoPrestamo)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("TEST")
                .updatedBy("TEST")
                .active(active)
                .build();

        // Then
        assertEquals(idSolicitud, solicitud.getIdSolicitud());
        assertEquals(monto, solicitud.getMonto());
        assertEquals(plazo, solicitud.getPlazo());
        assertEquals(email, solicitud.getEmail());
        assertEquals(documento, solicitud.getDocumentoIdentidad());
        assertEquals(idEstado, solicitud.getIdEstado());
        assertEquals(idTipoPrestamo, solicitud.getIdTipoPrestamo());
        assertEquals(now, solicitud.getCreatedAt());
        assertEquals(now, solicitud.getUpdatedAt());
        assertEquals("TEST", solicitud.getCreatedBy());
        assertEquals("TEST", solicitud.getUpdatedBy());
        assertEquals(active, solicitud.getActive());
    }
}