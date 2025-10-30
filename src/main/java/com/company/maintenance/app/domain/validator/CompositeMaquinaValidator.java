package com.company.maintenance.app.domain.validator;

import java.util.List;
import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CompositeMaquinaValidator implements MaquinaValidator {
    private final List<MaquinaValidator> validators;

    public CompositeMaquinaValidator(List<MaquinaValidator> validators) {
        this.validators = validators;
    }

    @Override
    public Mono<Void> validate(Maquina maquina) {
        return Flux.fromIterable(validators)
            .flatMap(validator -> validator.validate(maquina))
            .then();
    }
}