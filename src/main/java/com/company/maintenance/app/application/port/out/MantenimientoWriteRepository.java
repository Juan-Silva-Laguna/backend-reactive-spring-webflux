package com.company.maintenance.app.application.port.out;

import com.company.maintenance.app.domain.model.Mantenimiento;
import reactor.core.publisher.Mono;

public interface MantenimientoWriteRepository {
    Mono<Mantenimiento> save(Mantenimiento mantenimiento);
    Mono<Void> deleteById(String id);
}