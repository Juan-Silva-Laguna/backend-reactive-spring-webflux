package com.company.maintenance.app.application.port.in;

import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface MaquinaUseCase {
    Mono<Maquina> createMaquina(String nombre, String modelo, List<String> mantenimientosIds);
    Mono<Maquina> findById(String id);
    Flux<Maquina> findAll();
    Mono<Maquina> findByNombre(String nombre);
    Mono<Maquina> updateMaquina(String id, String nombre, String modelo);
    Mono<Maquina> addMantenimientoToMaquina(String maquinaId, String mantenimientoId);
    Mono<Maquina> removeMantenimientoFromMaquina(String maquinaId, String mantenimientoId);
    Mono<Void> deleteById(String id);
}