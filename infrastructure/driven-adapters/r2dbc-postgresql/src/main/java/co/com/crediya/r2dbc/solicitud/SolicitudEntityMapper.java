package co.com.crediya.r2dbc.solicitud;

import co.com.crediya.model.solicitud.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class SolicitudEntityMapper {

    public SolicitudEntity toEntity(Solicitud solicitud) {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(solicitud.getIdSolicitud());
        entity.setMonto(solicitud.getMonto());
        entity.setPlazo(solicitud.getPlazo());
        entity.setEmail(solicitud.getEmail());
        entity.setIdEstado(solicitud.getIdEstado());
        entity.setIdTipoPrestamo(solicitud.getIdTipoPrestamo());
        // Campos de auditoría
        entity.setCreatedAt(solicitud.getCreatedAt());
        entity.setUpdatedAt(solicitud.getUpdatedAt());
        entity.setCreatedBy(solicitud.getCreatedBy());
        entity.setUpdatedBy(solicitud.getUpdatedBy());
        entity.setActive(solicitud.getActive());
        return entity;
    }

    public Solicitud toDomain(SolicitudEntity entity) {
        return Solicitud.builder()
                .idSolicitud(entity.getIdSolicitud())
                .monto(entity.getMonto())
                .plazo(entity.getPlazo())
                .email(entity.getEmail())
                .idEstado(entity.getIdEstado())
                .idTipoPrestamo(entity.getIdTipoPrestamo())
                // Campos de auditoría
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .active(entity.getActive())
                .build();
    }
}