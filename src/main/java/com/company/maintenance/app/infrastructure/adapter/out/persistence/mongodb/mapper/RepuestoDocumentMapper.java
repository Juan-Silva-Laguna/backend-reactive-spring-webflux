package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper;

import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;

public class RepuestoDocumentMapper {

    public static RepuestoDocument toDocument(Repuesto repuesto) {
        return new RepuestoDocument(
            repuesto.getId(),
            repuesto.getNombre(),
            repuesto.getPrecio() != null ? repuesto.getPrecio() : 0
        );
    }

    public static Repuesto toDomain(RepuestoDocument doc) {
        return new Repuesto(
            doc.getId(),
            doc.getNombre(),
            doc.getPrecio()
        );
    }
}

