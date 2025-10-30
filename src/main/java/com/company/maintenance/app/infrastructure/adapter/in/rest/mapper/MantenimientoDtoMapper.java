package com.company.maintenance.app.infrastructure.adapter.in.rest.mapper;

import java.util.Collections;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MantenimientoDtoMapper {
    
    private MantenimientoDtoMapper() {}

    /**
     * ✅ MAPPER REACTIVO
     */
    public static Mono<MantenimientoResponse> toResponseReactive(Mantenimiento mantenimiento) {
        if (mantenimiento.getRepuestos() == null || mantenimiento.getRepuestos().isEmpty()) {
            return Mono.just(new MantenimientoResponse(
                mantenimiento.getId(),
                mantenimiento.getFecha(),
                mantenimiento.getDescripcion(),
                mantenimiento.getPrecio(),
                mantenimiento.getPrecioMantenimiento(),
                mantenimiento.getTotalRepuestos(),
                Collections.emptyList(),
                mantenimiento.getTipo(),
                mantenimiento.getMaquinaId()
            ));
        }

        return Flux.fromIterable(mantenimiento.getRepuestos())
            .map(r -> new RepuestoResponse(r.getId(), r.getNombre(), r.getPrecio()))
            .collectList()
            .map(repuestos -> new MantenimientoResponse(
                mantenimiento.getId(),
                mantenimiento.getFecha(),
                mantenimiento.getDescripcion(),
                mantenimiento.getPrecio(),
                mantenimiento.getPrecioMantenimiento(),
                mantenimiento.getTotalRepuestos(),
                repuestos,
                mantenimiento.getTipo(),
                mantenimiento.getMaquinaId()
            ));
    }

    /**
     * ⚠️ MAPPER SÍNCRONO (para compatibilidad)
     */
    public static MantenimientoResponse toResponse(Mantenimiento mantenimiento) {
        return new MantenimientoResponse(
            mantenimiento.getId(),
            mantenimiento.getFecha(),
            mantenimiento.getDescripcion(),
            mantenimiento.getPrecio(),
            mantenimiento.getPrecioMantenimiento(),
            mantenimiento.getTotalRepuestos(),
            mantenimiento.getRepuestos() != null
                ? mantenimiento.getRepuestos().stream()
                    .map(r -> new RepuestoResponse(r.getId(), r.getNombre(), r.getPrecio()))
                    .toList()
                : Collections.emptyList(),
            mantenimiento.getTipo(),
            mantenimiento.getMaquinaId()
        );
    }
}
