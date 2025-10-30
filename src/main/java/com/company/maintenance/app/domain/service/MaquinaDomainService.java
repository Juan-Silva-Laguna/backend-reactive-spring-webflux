package com.company.maintenance.app.domain.service;

import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.domain.model.Repuesto;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public class MaquinaDomainService {
    
    /**
     * Agrega un repuesto a TODOS los mantenimientos de forma reactiva
     */
    public Mono<Maquina> addRepuestoToAllMantenimientos(Maquina maquina, Repuesto repuesto) {
        if (repuesto == null) {
            return Mono.error(new IllegalArgumentException("El repuesto no puede ser nulo"));
        }

        return Flux.fromIterable(maquina.getMantenimientos())
            .map(mantenimiento -> mantenimiento
                .withRepuesto(repuesto)
                .withPrecioIncremented(repuesto.getPrecio())
            )
            .collectList()
            .map(mantenimientosActualizados -> 
                new Maquina(
                    maquina.getId(),
                    maquina.getNombre(),
                    maquina.getModelo(),
                    mantenimientosActualizados
                )
            );
    }

    /**
     * Remueve un repuesto de TODOS los mantenimientos
     */
    public Mono<Maquina> removeRepuestoFromAllMantenimientos(Maquina maquina, String repuestoId) {
        return Flux.fromIterable(maquina.getMantenimientos())
            .map(mantenimiento -> mantenimiento.withoutRepuesto(repuestoId))
            .collectList()
            .map(mantenimientosActualizados -> 
                new Maquina(
                    maquina.getId(),
                    maquina.getNombre(),
                    maquina.getModelo(),
                    mantenimientosActualizados
                )
            );
    }
}