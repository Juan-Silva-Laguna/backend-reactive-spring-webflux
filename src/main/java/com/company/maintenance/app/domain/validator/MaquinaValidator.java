package com.company.maintenance.app.domain.validator;

import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MaquinaValidator {
    Mono<Void> validate(Maquina maquina);
}