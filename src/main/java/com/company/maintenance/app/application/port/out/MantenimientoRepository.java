package com.company.maintenance.app.application.port.out;

import java.util.Date;

import com.company.maintenance.app.domain.model.Mantenimiento;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MantenimientoRepository {
    Mono<Mantenimiento> save(Mantenimiento mantenimiento);
    Mono<Mantenimiento> findById(String id);
    Flux<Mantenimiento> findAll();
    Flux<Mantenimiento> findByTipo(String tipo);
    Flux<Mantenimiento> findByFechaBetween(Date startDate, Date endDate);
    Flux<Mantenimiento> findByPrecioBetween(Double minPrice, Double maxPrice);
    Mono<Void> deleteById(String id);
    Mono<Boolean> existsById(String id);
}
