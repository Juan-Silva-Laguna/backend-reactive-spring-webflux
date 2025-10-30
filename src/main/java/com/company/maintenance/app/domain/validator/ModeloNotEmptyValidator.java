package com.company.maintenance.app.domain.validator;

import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Mono;

public class ModeloNotEmptyValidator implements MaquinaValidator {
    @Override
    public Mono<Void> validate(Maquina maquina) {
        if (maquina.getModelo() == null || maquina.getModelo().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException(
                "El modelo no puede estar vacío"
            ));
        }
        return Mono.empty();
    }
}