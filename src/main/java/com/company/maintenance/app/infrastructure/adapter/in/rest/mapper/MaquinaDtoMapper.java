package com.company.maintenance.app.infrastructure.adapter.in.rest.mapper;

import java.util.stream.Collectors;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MaquinaResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;

public class MaquinaDtoMapper {
    
    private MaquinaDtoMapper() {}

    public static MaquinaResponse toResponse(Maquina maquina) {
    	
        return new MaquinaResponse(
        		maquina.getId(),
                maquina.getNombre(),
                maquina.getModelo(),
                maquina.getMantenimientos().stream()
                	.map(mant -> new MantenimientoResponse(
                			mant.getId(), 
                			mant.getFecha(), 
                			mant.getDescripcion(), 
                			mant.getPrecio(), 
                			mant.getRepuestos().stream()
	                            .map(r -> new RepuestoResponse(r.getId(), r.getNombre(), r.getPrecio()))
	                            .collect(Collectors.toList()),
                            mant.getTipo()))
	                .collect(Collectors.toList())
        );
    }
}