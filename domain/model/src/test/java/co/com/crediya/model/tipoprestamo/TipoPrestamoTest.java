package co.com.crediya.model.tipoprestamo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TipoPrestamoTest {

    @Test
    void deberiaCrearTipoPrestamoConBuilder() {
        // Given
        Integer idTipoPrestamo = 1;
        String nombre = "PERSONAL";
        BigDecimal montoMinimo = new BigDecimal("500000");
        BigDecimal montoMaximo = new BigDecimal("5000000");
        BigDecimal tasaInteres = new BigDecimal("15.5");
        Boolean validacionAutomatica = true;
        LocalDateTime now = LocalDateTime.now();

        // When
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(idTipoPrestamo)
                .nombre(nombre)
                .montoMinimo(montoMinimo)
                .montoMaximo(montoMaximo)
                .tasaInteres(tasaInteres)
                .validacionAutomatica(validacionAutomatica)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .active(true)
                .build();

        // Then
        assertNotNull(tipoPrestamo);
        assertEquals(idTipoPrestamo, tipoPrestamo.getIdTipoPrestamo());
        assertEquals(nombre, tipoPrestamo.getNombre());
        assertEquals(montoMinimo, tipoPrestamo.getMontoMinimo());
        assertEquals(montoMaximo, tipoPrestamo.getMontoMaximo());
        assertEquals(tasaInteres, tipoPrestamo.getTasaInteres());
        assertEquals(validacionAutomatica, tipoPrestamo.getValidacionAutomatica());
        assertEquals(now, tipoPrestamo.getCreatedAt());
        assertEquals(now, tipoPrestamo.getUpdatedAt());
        assertEquals("SYSTEM", tipoPrestamo.getCreatedBy());
        assertEquals("SYSTEM", tipoPrestamo.getUpdatedBy());
        assertTrue(tipoPrestamo.getActive());
    }

    @Test
    void deberiaModificarTipoPrestamoConToBuilder() {
        // Given
        TipoPrestamo tipoPrestamoOriginal = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("PERSONAL")
                .montoMinimo(new BigDecimal("500000"))
                .montoMaximo(new BigDecimal("5000000"))
                .tasaInteres(new BigDecimal("15.5"))
                .validacionAutomatica(true)
                .active(true)
                .build();

        // When
        TipoPrestamo tipoPrestamoModificado = tipoPrestamoOriginal.toBuilder()
                .tasaInteres(new BigDecimal("14.0"))
                .updatedAt(LocalDateTime.now())
                .updatedBy("ADMIN")
                .build();

        // Then
        assertEquals(tipoPrestamoOriginal.getIdTipoPrestamo(), tipoPrestamoModificado.getIdTipoPrestamo());
        assertEquals(tipoPrestamoOriginal.getNombre(), tipoPrestamoModificado.getNombre());
        assertEquals(new BigDecimal("14.0"), tipoPrestamoModificado.getTasaInteres());
        assertEquals("ADMIN", tipoPrestamoModificado.getUpdatedBy());
        assertNotEquals(tipoPrestamoOriginal.getTasaInteres(), tipoPrestamoModificado.getTasaInteres());
    }

    @Test
    void deberiaManejarValoresNulos() {
        // When
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder().build();

        // Then
        assertNotNull(tipoPrestamo);
        assertNull(tipoPrestamo.getIdTipoPrestamo());
        assertNull(tipoPrestamo.getNombre());
        assertNull(tipoPrestamo.getMontoMinimo());
        assertNull(tipoPrestamo.getMontoMaximo());
        assertNull(tipoPrestamo.getTasaInteres());
        assertNull(tipoPrestamo.getValidacionAutomatica());
    }

    @Test
    void deberiaValidarRangoDeMontos() {
        // Given
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .montoMinimo(new BigDecimal("500000"))
                .montoMaximo(new BigDecimal("5000000"))
                .build();

        // When & Then
        assertTrue(tipoPrestamo.getMontoMaximo().compareTo(tipoPrestamo.getMontoMinimo()) > 0);
    }

    @Test
    void deberiaValidarEqualsYHashCode() {
        // Given
        TipoPrestamo tipo1 = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("PERSONAL")
                .montoMinimo(new BigDecimal("500000"))
                .tasaInteres(new BigDecimal("15.5"))
                .build();

        TipoPrestamo tipo2 = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("PERSONAL")
                .montoMinimo(new BigDecimal("500000"))
                .tasaInteres(new BigDecimal("15.5"))
                .build();

        TipoPrestamo tipo3 = TipoPrestamo.builder()
                .idTipoPrestamo(2)
                .nombre("HIPOTECARIO")
                .montoMinimo(new BigDecimal("10000000"))
                .tasaInteres(new BigDecimal("12.0"))
                .build();

        // Then
        assertEquals(tipo1, tipo2);
        assertNotEquals(tipo1, tipo3);
        assertEquals(tipo1.hashCode(), tipo2.hashCode());
        assertNotEquals(tipo1, null);
        assertNotEquals(tipo1, "string");
    }

    @Test
    void deberiaValidarToString() {
        // Given
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(1)
                .nombre("PERSONAL")
                .montoMinimo(new BigDecimal("500000"))
                .tasaInteres(new BigDecimal("15.5"))
                .build();

        // When
        String toString = tipoPrestamo.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("PERSONAL"));
        assertTrue(toString.contains("500000"));
        assertTrue(toString.contains("15.5"));
    }

    @Test
    void deberiaValidarCamposCompletos() {
        // Given
        Integer idTipoPrestamo = 3;
        String nombre = "VEHICULAR";
        BigDecimal montoMinimo = new BigDecimal("2000000");
        BigDecimal montoMaximo = new BigDecimal("50000000");
        BigDecimal tasaInteres = new BigDecimal("18.0");
        Boolean validacionAutomatica = false;
        LocalDateTime now = LocalDateTime.now();
        Boolean active = true;

        // When
        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(idTipoPrestamo)
                .nombre(nombre)
                .montoMinimo(montoMinimo)
                .montoMaximo(montoMaximo)
                .tasaInteres(tasaInteres)
                .validacionAutomatica(validacionAutomatica)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("TEST")
                .updatedBy("TEST")
                .active(active)
                .build();

        // Then
        assertEquals(idTipoPrestamo, tipoPrestamo.getIdTipoPrestamo());
        assertEquals(nombre, tipoPrestamo.getNombre());
        assertEquals(montoMinimo, tipoPrestamo.getMontoMinimo());
        assertEquals(montoMaximo, tipoPrestamo.getMontoMaximo());
        assertEquals(tasaInteres, tipoPrestamo.getTasaInteres());
        assertEquals(validacionAutomatica, tipoPrestamo.getValidacionAutomatica());
        assertEquals(now, tipoPrestamo.getCreatedAt());
        assertEquals(now, tipoPrestamo.getUpdatedAt());
        assertEquals("TEST", tipoPrestamo.getCreatedBy());
        assertEquals("TEST", tipoPrestamo.getUpdatedBy());
        assertEquals(active, tipoPrestamo.getActive());
    }

    @Test
    void deberiaValidarComparacionDeMontos() {
        // Given
        TipoPrestamo tipoConMontosIguales = TipoPrestamo.builder()
                .montoMinimo(new BigDecimal("1000000"))
                .montoMaximo(new BigDecimal("1000000"))
                .build();

        TipoPrestamo tipoConMontoMinimoMayor = TipoPrestamo.builder()
                .montoMinimo(new BigDecimal("2000000"))
                .montoMaximo(new BigDecimal("1000000"))
                .build();

        // When & Then
        assertEquals(0, tipoConMontosIguales.getMontoMaximo().compareTo(tipoConMontosIguales.getMontoMinimo()));
        assertTrue(tipoConMontoMinimoMayor.getMontoMinimo().compareTo(tipoConMontoMinimoMayor.getMontoMaximo()) > 0);
    }
}