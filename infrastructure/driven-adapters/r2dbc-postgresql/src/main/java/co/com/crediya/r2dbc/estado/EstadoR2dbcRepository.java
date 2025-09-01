package co.com.crediya.r2dbc.estado;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EstadoR2dbcRepository extends ReactiveCrudRepository<EstadoEntity, Integer> {
    Mono<EstadoEntity> findByNombre(String nombre);
}