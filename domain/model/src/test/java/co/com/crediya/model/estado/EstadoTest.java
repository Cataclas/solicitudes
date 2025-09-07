package co.com.crediya.model.estado;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    @Test
    void deberiaCrearEstadoConBuilder() {
        // Given
        Integer idEstado = 1;
        String nombre = "PENDIENTE";
        String descripcion = "Solicitud pendiente de revisión";
        LocalDateTime now = LocalDateTime.now();

        // When
        Estado estado = Estado.builder()
                .idEstado(idEstado)
                .nombre(nombre)
                .descripcion(descripcion)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .active(true)
                .build();

        // Then
        assertNotNull(estado);
        assertEquals(idEstado, estado.getIdEstado());
        assertEquals(nombre, estado.getNombre());
        assertEquals(descripcion, estado.getDescripcion());
        assertEquals(now, estado.getCreatedAt());
        assertEquals(now, estado.getUpdatedAt());
        assertEquals("SYSTEM", estado.getCreatedBy());
        assertEquals("SYSTEM", estado.getUpdatedBy());
        assertTrue(estado.getActive());
    }

    @Test
    void deberiaModificarEstadoConToBuilder() {
        // Given
        Estado estadoOriginal = Estado.builder()
                .idEstado(1)
                .nombre("PENDIENTE")
                .descripcion("Solicitud pendiente")
                .active(true)
                .build();

        // When
        Estado estadoModificado = estadoOriginal.toBuilder()
                .descripcion("Solicitud pendiente de revisión")
                .updatedAt(LocalDateTime.now())
                .updatedBy("ADMIN")
                .build();

        // Then
        assertEquals(estadoOriginal.getIdEstado(), estadoModificado.getIdEstado());
        assertEquals(estadoOriginal.getNombre(), estadoModificado.getNombre());
        assertEquals("Solicitud pendiente de revisión", estadoModificado.getDescripcion());
        assertEquals("ADMIN", estadoModificado.getUpdatedBy());
        assertNotEquals(estadoOriginal.getDescripcion(), estadoModificado.getDescripcion());
    }

    @Test
    void deberiaManejarValoresNulos() {
        // When
        Estado estado = Estado.builder().build();

        // Then
        assertNotNull(estado);
        assertNull(estado.getIdEstado());
        assertNull(estado.getNombre());
        assertNull(estado.getDescripcion());
    }

    @Test
    void deberiaValidarEqualsYHashCode() {
        // Given
        Estado estado1 = Estado.builder()
                .idEstado(1)
                .nombre("PENDIENTE")
                .descripcion("Pendiente")
                .build();

        Estado estado2 = Estado.builder()
                .idEstado(1)
                .nombre("PENDIENTE")
                .descripcion("Pendiente")
                .build();

        Estado estado3 = Estado.builder()
                .idEstado(2)
                .nombre("APROBADO")
                .descripcion("Aprobado")
                .build();

        // Then
        assertEquals(estado1, estado2);
        assertNotEquals(estado1, estado3);
        assertEquals(estado1.hashCode(), estado2.hashCode());
        assertNotEquals(estado1, null);
        assertNotEquals(estado1, "string");
    }

    @Test
    void deberiaValidarToString() {
        // Given
        Estado estado = Estado.builder()
                .idEstado(1)
                .nombre("PENDIENTE")
                .descripcion("Solicitud pendiente")
                .build();

        // When
        String toString = estado.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("PENDIENTE"));
        assertTrue(toString.contains("Solicitud pendiente"));
    }

    @Test
    void deberiaValidarCamposCompletos() {
        // Given
        Integer idEstado = 5;
        String nombre = "RECHAZADO";
        String descripcion = "Solicitud rechazada";
        LocalDateTime now = LocalDateTime.now();
        Boolean active = false;

        // When
        Estado estado = Estado.builder()
                .idEstado(idEstado)
                .nombre(nombre)
                .descripcion(descripcion)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("TEST")
                .updatedBy("TEST")
                .active(active)
                .build();

        // Then
        assertEquals(idEstado, estado.getIdEstado());
        assertEquals(nombre, estado.getNombre());
        assertEquals(descripcion, estado.getDescripcion());
        assertEquals(now, estado.getCreatedAt());
        assertEquals(now, estado.getUpdatedAt());
        assertEquals("TEST", estado.getCreatedBy());
        assertEquals("TEST", estado.getUpdatedBy());
        assertEquals(active, estado.getActive());
    }
}