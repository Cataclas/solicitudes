package co.com.crediya.r2dbc.tipoprestamo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TipoPrestamoR2dbcRepository extends ReactiveCrudRepository<TipoPrestamoEntity, Integer> {
}