package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MaquinaDocument;

public final class MaquinaDocumentMapper {

    private MaquinaDocumentMapper() {}

    // Document -> Domain
    public static Maquina toDomain(MaquinaDocument doc) {
        if (doc == null) return null;

        List<Mantenimiento> mantenimientos = (doc.getMantenimientos() == null)
            ? Collections.emptyList()
            : doc.getMantenimientos().stream()
                .map(MantenimientoDocumentMapper::toDomain)
                .collect(Collectors.toList());

        return new Maquina(
            doc.getId(),
            doc.getNombre(),
            doc.getModelo(),
            mantenimientos
        );
    }

    public static MaquinaDocument toDocument(Maquina mant) {
        if (mant == null) return null;

        List<MantenimientoDocument> mantenimientosDoc = (mant.getMantenimientos() == null)
            ? Collections.emptyList()
            : mant.getMantenimientos().stream()
                .map(MantenimientoDocumentMapper::toDocument)
                .collect(Collectors.toList());

        return new MaquinaDocument(
            mant.getId(),
            mant.getNombre(),
            mant.getModelo(),
            mantenimientosDoc
        );
    }
}
