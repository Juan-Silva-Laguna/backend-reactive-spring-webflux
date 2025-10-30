package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper;

import org.springframework.stereotype.Component;
import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
import reactor.core.publisher.Mono;

@Component
public class RepuestoDocumentMapper {

    /**
     * ✅ CONVERSIÓN REACTIVA: Document -> Domain
     * Como Repuesto no tiene colecciones anidadas, la conversión es simple
     */
    public Mono<Repuesto> toDomainReactive(RepuestoDocument doc) {
        if (doc == null) {
            return Mono.empty();
        }
        return Mono.just(new Repuesto(
            doc.getId(),
            doc.getNombre(),
            doc.getPrecio()
        ));
    }

    /**
     * ⚠️ CONVERSIÓN SÍNCRONA (para compatibilidad)
     */
    public Repuesto toDomain(RepuestoDocument doc) {
        if (doc == null) return null;
        return new Repuesto(
            doc.getId(),
            doc.getNombre(),
            doc.getPrecio()
        );
    }

    /**
     * ✅ CONVERSIÓN REACTIVA: Domain -> Document
     */
    public Mono<RepuestoDocument> toDocumentReactive(Repuesto repuesto) {
        if (repuesto == null) {
            return Mono.empty();
        }
        return Mono.just(new RepuestoDocument(
            repuesto.getId(),
            repuesto.getNombre(),
            repuesto.getPrecio()
        ));
    }

    /**
     * ⚠️ CONVERSIÓN SÍNCRONA (para compatibilidad)
     */
    public RepuestoDocument toDocument(Repuesto repuesto) {
        if (repuesto == null) return null;
        return new RepuestoDocument(
            repuesto.getId(),
            repuesto.getNombre(),
            repuesto.getPrecio()
        );
    }
}