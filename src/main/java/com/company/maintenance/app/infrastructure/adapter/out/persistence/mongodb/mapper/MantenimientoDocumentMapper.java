package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MantenimientoDocumentMapper {

    private final RepuestoDocumentMapper repuestoMapper;

    public MantenimientoDocumentMapper(RepuestoDocumentMapper repuestoMapper) {
        this.repuestoMapper = repuestoMapper;
    }

    /**
     * ✅ CONVERSIÓN REACTIVA: Document -> Domain
     */
    public Mono<Mantenimiento> toDomainReactive(MantenimientoDocument doc) {
        if (doc == null) {
            return Mono.empty();
        }

        // Si no hay repuestos, retorna directamente
        if (doc.getRepuestos() == null || doc.getRepuestos().isEmpty()) {
            return Mono.just(new Mantenimiento(
                doc.getId(),
                doc.getFecha(),
                doc.getDescripcion(),
                doc.getPrecio(),
                Collections.emptyList(),
                doc.getTipo()
            ).withMaquinaId(doc.getMaquinaId()));
        }

        // Convierte repuestos de forma reactiva
        return Flux.fromIterable(doc.getRepuestos())
            .flatMap(repuestoMapper::toDomainReactive)
            .collectList()
            .map(repuestos -> new Mantenimiento(
                doc.getId(),
                doc.getFecha(),
                doc.getDescripcion(),
                doc.getPrecio(),
                repuestos,
                doc.getTipo()
            ).withMaquinaId(doc.getMaquinaId()));
    }

    /**
     * ⚠️ CONVERSIÓN SÍNCRONA (para compatibilidad)
     * Debe ejecutarse en Schedulers.boundedElastic()
     */
    public Mantenimiento toDomain(MantenimientoDocument doc) {
        if (doc == null) return null;

        List<Repuesto> repuestos = (doc.getRepuestos() == null)
            ? Collections.emptyList()
            : doc.getRepuestos().stream()
                .map(repuestoMapper::toDomain)
                .toList();

        return new Mantenimiento(
        	    doc.getId(),
        	    doc.getFecha(),
        	    doc.getDescripcion(),
        	    doc.getPrecio(),
        	    repuestos,
        	    doc.getTipo()
        	).withMaquinaId(doc.getMaquinaId());
    }

    /**
     * ✅ CONVERSIÓN REACTIVA: Domain -> Document
     */
    public Mono<MantenimientoDocument> toDocumentReactive(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            return Mono.empty();
        }

        // Si no hay repuestos
        if (mantenimiento.getRepuestos() == null || mantenimiento.getRepuestos().isEmpty()) {
            return Mono.just(new MantenimientoDocument(
                mantenimiento.getId(),
                mantenimiento.getFecha(),
                mantenimiento.getDescripcion(),
                mantenimiento.getPrecio(),
                Collections.emptyList(),
                mantenimiento.getTipo(),
                mantenimiento.getMaquinaId()
            ));
        }

        // Convierte repuestos de forma reactiva
        return Flux.fromIterable(mantenimiento.getRepuestos())
            .flatMap(repuestoMapper::toDocumentReactive)
            .collectList()
            .map(repuestosDoc -> new MantenimientoDocument(
                mantenimiento.getId(),
                mantenimiento.getFecha(),
                mantenimiento.getDescripcion(),
                mantenimiento.getPrecio(),
                repuestosDoc,
                mantenimiento.getTipo(),
                mantenimiento.getMaquinaId()
            ));
    }

    /**
     * ⚠️ CONVERSIÓN SÍNCRONA (para compatibilidad)
     */
    public MantenimientoDocument toDocument(Mantenimiento mantenimiento) {
        if (mantenimiento == null) return null;

        List<RepuestoDocument> repuestosDoc = (mantenimiento.getRepuestos() == null)
            ? Collections.emptyList()
            : mantenimiento.getRepuestos().stream()
                .map(repuestoMapper::toDocument)
                .toList();

        return new MantenimientoDocument(
            mantenimiento.getId(),
            mantenimiento.getFecha(),
            mantenimiento.getDescripcion(),
            mantenimiento.getPrecio(),
            repuestosDoc,
            mantenimiento.getTipo(),
            mantenimiento.getMaquinaId()
        );
    }
}