package co.com.crediya.r2dbc.estado;

import co.com.crediya.model.estado.Estado;
import org.springframework.stereotype.Component;

@Component
public class EstadoEntityMapper {

    public Estado toDomain(EstadoEntity entity) {
        return Estado.builder()
                .idEstado(entity.getIdEstado())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .active(entity.getActive())
                .build();
    }
}