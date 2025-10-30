package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MaquinaDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MaquinaDocumentMapper {
    
    private final MantenimientoDocumentMapper mantenimientoMapper;

    public MaquinaDocumentMapper(MantenimientoDocumentMapper mantenimientoMapper) {
        this.mantenimientoMapper = mantenimientoMapper;
    }

    /**
     * ✅ CONVERSIÓN REACTIVA: Document -> Domain
     * No usa streams bloqueantes
     */
    public Mono<Maquina> toDomainReactive(MaquinaDocument doc) {
        if (doc == null) {
            return Mono.empty();
        }

        if (doc.getMantenimientos() == null || doc.getMantenimientos().isEmpty()) {
            return Mono.just(new Maquina(
                doc.getId(),
                doc.getNombre(),
                doc.getModelo(),
                Collections.emptyList()
            ));
        }

        return Flux.fromIterable(doc.getMantenimientos())
            .flatMap(mantenimientoMapper::toDomainReactive)
            .collectList()
            .map(mantenimientos -> new Maquina(
                doc.getId(),
                doc.getNombre(),
                doc.getModelo(),
                mantenimientos
            ));
    }

    /**
     * ⚠️ CONVERSIÓN SÍNCRONA (para compatibilidad)
     * Debe ejecutarse en Schedulers.boundedElastic()
     */
    public Maquina toDomain(MaquinaDocument doc) {
        if (doc == null) return null;

        List<Mantenimiento> mantenimientos = (doc.getMantenimientos() == null)
            ? Collections.emptyList()
            : doc.getMantenimientos().stream()
                .map(mantenimientoMapper::toDomain)
                .toList(); // Java 16+ (más eficiente que collect)

        return new Maquina(
            doc.getId(),
            doc.getNombre(),
            doc.getModelo(),
            mantenimientos
        );
    }

    /**
     * ✅ CONVERSIÓN REACTIVA: Domain -> Document
     */
    public Mono<MaquinaDocument> toDocumentReactive(Maquina maquina) {
        if (maquina == null) {
            return Mono.empty();
        }

        if (maquina.getMantenimientos() == null || maquina.getMantenimientos().isEmpty()) {
            return Mono.just(new MaquinaDocument(
                maquina.getId(),
                maquina.getNombre(),
                maquina.getModelo(),
                Collections.emptyList()
            ));
        }

        return Flux.fromIterable(maquina.getMantenimientos())
            .flatMap(mantenimientoMapper::toDocumentReactive)
            .collectList()
            .map(mantenimientosDoc -> new MaquinaDocument(
                maquina.getId(),
                maquina.getNombre(),
                maquina.getModelo(),
                mantenimientosDoc
            ));
    }

    /**
     * ⚠️ CONVERSIÓN SÍNCRONA (para compatibilidad)
     */
    public MaquinaDocument toDocument(Maquina maquina) {
        if (maquina == null) return null;

        List<MantenimientoDocument> mantenimientosDoc = (maquina.getMantenimientos() == null)
            ? Collections.emptyList()
            : maquina.getMantenimientos().stream()
                .map(mantenimientoMapper::toDocument)
                .toList();

        return new MaquinaDocument(
            maquina.getId(),
            maquina.getNombre(),
            maquina.getModelo(),
            mantenimientosDoc
        );
    }
}