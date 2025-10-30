package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Mantenimiento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MantenimientoReadRepository {
    Mono<Mantenimiento> findById(String id);
    Flux<Mantenimiento> findAll();
    Flux<Mantenimiento> findByFecha(String fecha);
    Flux<Mantenimiento> findByFechaBetween(String startDate, String endDate);
    Flux<Mantenimiento> findByTipo(String tipo);
    Flux<Mantenimiento> findByPrecioBetween(Double minPrecio, Double maxPrecio);
    Mono<Boolean> existsById(String id);
}