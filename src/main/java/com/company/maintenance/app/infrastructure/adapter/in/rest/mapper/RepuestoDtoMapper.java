package com.company.maintenance.app.infrastructure.adapter.in.rest.mapper;

import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;

public class RepuestoDtoMapper {
	
    public static RepuestoResponse toResponse(Repuesto repuesto) {
        return new RepuestoResponse(
            repuesto.getId(),
            repuesto.getNombre(),
            repuesto.getPrecio()
        );
    }
}
