package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;

public final class MantenimientoDocumentMapper {

    private MantenimientoDocumentMapper() {}
    private static final SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    // Document -> Domain
    public static Mantenimiento toDomain(MantenimientoDocument doc) {
        if (doc == null) return null;

        List<Repuesto> repuestos = (doc.getRepuestos() == null)
            ? Collections.emptyList()
            : doc.getRepuestos().stream()
                .map(RepuestoDocumentMapper::toDomain)
                .collect(Collectors.toList());
        
        Date fecha = null;
        try {
            fecha = formatoISO.parse(doc.getFecha());
        } catch (ParseException e) {
            throw new RuntimeException("Error al parsear la fecha: " + doc.getFecha(), e);
        }
        
        return new Mantenimiento(
            doc.getId(),
            fecha,
            doc.getDescripcion(),
            doc.getPrecio(),
            repuestos,
            doc.getTipo(),
            doc.getMaquinaId()
        );
    }

    // Domain -> Document (nombre: toDocument para ajustarse a tu adapter)
    public static MantenimientoDocument toDocument(Mantenimiento mant) {
        if (mant == null) return null;

        List<RepuestoDocument> repuestosDoc = (mant.getRepuestos() == null)
            ? Collections.emptyList()
            : mant.getRepuestos().stream()
                .map(RepuestoDocumentMapper::toDocument)
                .collect(Collectors.toList());

        return new MantenimientoDocument(
            mant.getId(),
            formatoISO.format(mant.getFecha()),
            mant.getDescripcion(),
            mant.getPrecio(),
            repuestosDoc,
            mant.getTipo(),
            mant.getMaquinaId()
        );
    }
}
