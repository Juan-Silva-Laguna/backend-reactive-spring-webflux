package com.company.maintenance.app.domain.validator;

import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Mono;

public class NombreNotNullValidator implements MaquinaValidator {
    @Override
    public Mono<Void> validate(Maquina maquina) {
        if (maquina.getNombre() == null || maquina.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException(
                "El nombre de la máquina no puede ser nulo o vacío"
            ));
        }
        return Mono.empty();
    }
}