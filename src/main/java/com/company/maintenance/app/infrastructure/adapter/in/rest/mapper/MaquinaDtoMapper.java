package com.company.maintenance.app.infrastructure.adapter.in.rest.mapper;

import java.util.Collections;
import java.util.List;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MaquinaResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MaquinaDtoMapper {
    
    private MaquinaDtoMapper() {}

    /**
     * ✅ MAPPER REACTIVO - No usa streams bloqueantes
     */
    public static Mono<MaquinaResponse> toResponseReactive(Maquina maquina) {
        if (maquina.getMantenimientos() == null || maquina.getMantenimientos().isEmpty()) {
            return Mono.just(new MaquinaResponse(
                maquina.getId(),
                maquina.getNombre(),
                maquina.getModelo(),
                Collections.emptyList()
            ));
        }

        return Flux.fromIterable(maquina.getMantenimientos())
            .flatMap(mant -> {
                // Mapeo reactivo de repuestos
                if (mant.getRepuestos() == null || mant.getRepuestos().isEmpty()) {
                    return Mono.just(new MantenimientoResponse(
                        mant.getId(),
                        mant.getFecha(),
                        mant.getDescripcion(),
                        mant.getPrecio(),
                        mant.getPrecioMantenimiento(),
                        mant.getTotalRepuestos(),
                        Collections.emptyList(),
                        mant.getTipo(),
                        mant.getMaquinaId()
                    ));
                }

                return Flux.fromIterable(mant.getRepuestos())
                    .map(r -> new RepuestoResponse(r.getId(), r.getNombre(), r.getPrecio()))
                    .collectList()
                    .map(repuestos -> new MantenimientoResponse(
                        mant.getId(),
                        mant.getFecha(),
                        mant.getDescripcion(),
                        mant.getPrecio(),
                        mant.getPrecioMantenimiento(),
                        mant.getTotalRepuestos(),
                        repuestos,
                        mant.getTipo(),
                        mant.getMaquinaId()
                    ));
            })
            .collectList()
            .map(mantenimientos -> new MaquinaResponse(
                maquina.getId(),
                maquina.getNombre(),
                maquina.getModelo(),
                mantenimientos
            ));
    }

    /**
     * ⚠️ MAPPER SÍNCRONO (para compatibilidad)
     * Debe usarse dentro de operadores map() en flujos reactivos
     */
    public static MaquinaResponse toResponse(Maquina maquina) {
        List<MantenimientoResponse> mantenimientos = maquina.getMantenimientos() != null
            ? maquina.getMantenimientos().stream()
                .map(mant -> new MantenimientoResponse(
                    mant.getId(),
                    mant.getFecha(),
                    mant.getDescripcion(),
                    mant.getPrecio(),

                    mant.getPrecioMantenimiento(),
                    mant.getTotalRepuestos(),
                    mant.getRepuestos() != null 
                        ? mant.getRepuestos().stream()
                            .map(r -> new RepuestoResponse(r.getId(), r.getNombre(), r.getPrecio()))
                            .toList()
                        : Collections.emptyList(),
                    mant.getTipo(),
                    mant.getMaquinaId()
                ))
                .toList()
            : Collections.emptyList();

        return new MaquinaResponse(
            maquina.getId(),
            maquina.getNombre(),
            maquina.getModelo(),
            mantenimientos
        );
    }
}
