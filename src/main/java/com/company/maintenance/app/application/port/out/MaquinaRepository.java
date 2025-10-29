package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Maquina;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MaquinaRepository {
    Mono<Maquina> save(Maquina Maquina);
    Mono<Maquina> findById(String id);
    Flux<Maquina> findAll();
    Mono<Maquina> findByNombre(String nombre);
    Mono<Void> deleteById(String id);
    Mono<Boolean> existsById(String id);
    Mono<Boolean> existsByNombre(String nombre);
}
