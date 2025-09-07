package co.com.crediya.r2dbc.estado;

import co.com.crediya.model.estado.Estado;
import co.com.crediya.model.estado.gateways.EstadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EstadoRepositoryAdapter implements EstadoRepository {

    private final EstadoR2dbcRepository r2dbcRepository;
    private final EstadoEntityMapper mapper;

    @Override
    public Mono<Estado> findByNombre(String nombre) {
        log.debug("Buscando estado por nombre: {}", nombre);
        return r2dbcRepository.findByNombre(nombre)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Estado> findById(Integer idEstado) {
        log.debug("Buscando estado por ID: {}", idEstado);
        return r2dbcRepository.findById(idEstado)
                .map(mapper::toDomain);
    }
}