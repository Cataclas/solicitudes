package co.com.crediya.r2dbc.tipoprestamo;

import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TipoPrestamoRepositoryAdapter implements TipoPrestamoRepository {

    private final TipoPrestamoR2dbcRepository r2dbcRepository;

    @Override
    public Mono<TipoPrestamo> findById(Integer idTipoPrestamo) {
        log.debug("Buscando tipo de préstamo por ID: {}", idTipoPrestamo);
        return r2dbcRepository.findById(idTipoPrestamo)
                .map(this::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Integer idTipoPrestamo) {
        log.debug("Verificando existencia de tipo de préstamo: {}", idTipoPrestamo);
        return r2dbcRepository.existsById(idTipoPrestamo);
    }

    private TipoPrestamo toDomain(TipoPrestamoEntity entity) {
        return TipoPrestamo.builder()
                .idTipoPrestamo(entity.getIdTipoPrestamo())
                .nombre(entity.getNombre())
                .montoMinimo(entity.getMontoMinimo())
                .montoMaximo(entity.getMontoMaximo())
                .tasaInteres(entity.getTasaInteres())
                .validacionAutomatica(entity.getValidacionAutomatica())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .active(entity.getActive())
                .build();
    }
}