package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Repuesto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepuestoReadRepository {
    Mono<Repuesto> findById(String id);
    Mono<Repuesto> findByNombre(String nombre);
    Flux<Repuesto> findAll();
    Flux<Repuesto> findByPrecioBetween(Double minPrecio, Double maxPrecio);
    Mono<Boolean> existsById(String id);
    Mono<Boolean> existsByNombre(String nombre);
}
