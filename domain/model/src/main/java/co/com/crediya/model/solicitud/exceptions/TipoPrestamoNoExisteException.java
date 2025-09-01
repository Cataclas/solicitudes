package co.com.crediya.model.solicitud.exceptions;

public class TipoPrestamoNoExisteException extends RuntimeException {
    public TipoPrestamoNoExisteException(String mensaje) {
        super(mensaje);
    }
}