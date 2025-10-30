package com.company.maintenance.app.infrastructure.adapter.in.rest.mapper;

import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;
import reactor.core.publisher.Mono;

public class RepuestoDtoMapper {
    
    private RepuestoDtoMapper() {}

    public static Mono<RepuestoResponse> toResponseReactive(Repuesto repuesto) {
        return Mono.just(new RepuestoResponse(
            repuesto.getId(),
            repuesto.getNombre(),
            repuesto.getPrecio()
        ));
    }

    public static RepuestoResponse toResponse(Repuesto repuesto) {
        return new RepuestoResponse(
            repuesto.getId(),
            repuesto.getNombre(),
            repuesto.getPrecio()
        );
    }
}