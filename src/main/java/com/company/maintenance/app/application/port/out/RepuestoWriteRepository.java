package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Repuesto;
import reactor.core.publisher.Mono;

public interface RepuestoWriteRepository {
    Mono<Repuesto> save(Repuesto repuesto);
    Mono<Void> deleteById(String id);
}