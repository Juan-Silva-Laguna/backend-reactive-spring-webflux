package com.company.maintenance.app.application.port.in;

import com.company.maintenance.app.domain.model.Repuesto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepuestoUseCase {
    Mono<Repuesto> createRepuesto(String nombre, Double precio);


    Mono<Repuesto> findById(String id);
    Mono<Repuesto> findByNombre(String nombre);
    Flux<Repuesto> findAll();
    Flux<Repuesto> findByPriceRange(Double minPrice, Double maxPrice);


    Mono<Repuesto> updateRepuesto(String id, String nombre, Double precio);
    Mono<Repuesto> updatePrice(String id, Double newPrice);


    Mono<Void> deleteById(String id);


}
