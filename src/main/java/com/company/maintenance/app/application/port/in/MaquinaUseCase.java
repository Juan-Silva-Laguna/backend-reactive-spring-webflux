package com.company.maintenance.app.application.port.in;

import java.util.List;

import com.company.maintenance.app.domain.model.Maquina;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MaquinaUseCase {
    Mono<Maquina> createMaquina(String nombre, String modelo, List<String> mantenimientosIds);
    Mono<Maquina> findById(String id);
    Flux<Maquina> findAll();
    Mono<Maquina> updateMaquina(String id, String nombre, String modelo);
    Mono<Maquina> addMantenimientoToMaquina(String MaquinaId, String mantenimientoId);
    Mono<Maquina> removeMantenimientoFromMaquina(String MaquinaId, String mantenimientoId);
    Mono<Void> deleteById(String id);
    Mono<Maquina> findByNombre(String nombre);
}
