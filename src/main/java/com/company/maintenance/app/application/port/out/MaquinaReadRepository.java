package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MaquinaReadRepository {
    Mono<Maquina> findById(String id);
    Mono<Maquina> findByNombre(String nombre);
    Flux<Maquina> findAll();
    Mono<Boolean> existsById(String id);
    Mono<Boolean> existsByNombre(String nombre);
}