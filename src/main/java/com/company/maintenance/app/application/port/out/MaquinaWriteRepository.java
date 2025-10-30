package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Mono;

public interface MaquinaWriteRepository {
    Mono<Maquina> save(Maquina maquina);
    Mono<Void> deleteById(String id);
}