package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.EstadoDTO;
import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.dto.SolicitudResponseDTO;
import co.com.crediya.api.dto.TipoPrestamoDTO;
import co.com.crediya.model.estado.Estado;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exceptions.DatosInvalidosException;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {

    public Solicitud toDomain(SolicitudRequestDTO dto) {
        if (dto == null) {
            throw new DatosInvalidosException("Los datos de la solicitud son requeridos");
        }
        
        return Solicitud.builder()
                .monto(dto.getMonto())
                .plazo(dto.getPlazo())
                .email(dto.getEmail())
                .documentoIdentidad(dto.getDocumentoIdentidad())
                .idTipoPrestamo(dto.getIdTipoPrestamo())
                .build();
    }

    public SolicitudResponseDTO toResponse(Solicitud solicitud, Estado estado, TipoPrestamo tipoPrestamo) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setMonto(solicitud.getMonto());
        dto.setPlazo(solicitud.getPlazo());
        dto.setEmail(solicitud.getEmail());
        dto.setDocumentoIdentidad(solicitud.getDocumentoIdentidad());
        dto.setEstado(toEstadoDTO(estado));
        dto.setTipoPrestamo(toTipoPrestamoDTO(tipoPrestamo));
        dto.setCreatedAt(solicitud.getCreatedAt());
        dto.setActive(solicitud.getActive());
        return dto;
    }

    private EstadoDTO toEstadoDTO(Estado estado) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(estado.getIdEstado());
        dto.setNombre(estado.getNombre());
        dto.setDescripcion(estado.getDescripcion());
        return dto;
    }

    private TipoPrestamoDTO toTipoPrestamoDTO(TipoPrestamo tipoPrestamo) {
        TipoPrestamoDTO dto = new TipoPrestamoDTO();
        dto.setId(tipoPrestamo.getIdTipoPrestamo());
        dto.setNombre(tipoPrestamo.getNombre());
        dto.setMontoMinimo(tipoPrestamo.getMontoMinimo());
        dto.setMontoMaximo(tipoPrestamo.getMontoMaximo());
        dto.setTasaInteres(tipoPrestamo.getTasaInteres());
        return dto;
    }
}