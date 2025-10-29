package com.company.maintenance.app.infrastructure.adapter.in.rest.mapper;

import java.util.stream.Collectors;

import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;


public final class MantenimientoDtoMapper {

    private MantenimientoDtoMapper() {}

    public static MantenimientoResponse toResponse(Mantenimiento mantenimiento) {
        return new MantenimientoResponse(
            mantenimiento.getId(),
            mantenimiento.getFecha(),
            mantenimiento.getDescripcion(),
            mantenimiento.getPrecio(),
            mantenimiento.getRepuestos().stream()
                       .map(r -> new RepuestoResponse(r.getId(), r.getNombre(), r.getPrecio()))
                       .collect(Collectors.toList()),
            mantenimiento.getTipo()
        );
    }
}