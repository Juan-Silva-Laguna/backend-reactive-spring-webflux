package com.company.maintenance.app.application.port.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.company.maintenance.app.application.port.in.MaquinaUseCase;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.application.port.out.MantenimientoReadRepository;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.domain.exception.MaquinaException;
import com.company.maintenance.app.domain.exception.MantenimientoException;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.validator.MaquinaValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MaquinaService implements MaquinaUseCase {

    private final MaquinaRepository maquinaRepository;
    private final MantenimientoRepository mantenimientoRepository;
    private final MaquinaValidator validator;

    public MaquinaService(
            MaquinaRepository maquinaRepository,
            MantenimientoRepository mantenimientoRepository,
            MaquinaValidator validator) {
        this.maquinaRepository = maquinaRepository;
        this.mantenimientoRepository = mantenimientoRepository;
        this.validator = validator;
    }

    @Override
    public Mono<Maquina> createMaquina(String nombre, String modelo, List<String> mantenimientosIds) {
        // Caso 1: Sin mantenimientos
        if (mantenimientosIds == null || mantenimientosIds.isEmpty()) {
            Maquina maquina = new Maquina(nombre, modelo);
            return validator.validate(maquina)
                .then(maquinaRepository.save(maquina));
        }

        // Caso 2: Con mantenimientos - 100% reactivo
        return Flux.fromIterable(mantenimientosIds)
            .flatMap(id -> mantenimientoRepository.findById(id)
                .switchIfEmpty(Mono.error(MantenimientoException.notFound(id))) // ✅ Excepción correcta
            )
            .collectList()
            .map(mantenimientos -> new Maquina(nombre, modelo, mantenimientos))
            .flatMap(maquina -> validator.validate(maquina).thenReturn(maquina))
            .flatMap(maquinaRepository::save);
    }

    @Override
    public Mono<Maquina> findById(String id) {
        return maquinaRepository.findById(id)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(id)));
    }

    @Override
    public Flux<Maquina> findAll() {
        return maquinaRepository.findAll();
    }

    @Override
    public Mono<Maquina> findByNombre(String nombre) {
        return maquinaRepository.findByNombre(nombre)
            .switchIfEmpty(Mono.error(
                MaquinaException.notFound("No se encontró máquina con nombre: " + nombre)
            ));
    }

    @Override
    public Mono<Maquina> updateMaquina(String id, String nombre, String modelo) {
        return maquinaRepository.findById(id)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(id)))
            .map(maquina -> maquina.withNombreAndModelo(nombre, modelo)) // ✅ Inmutabilidad
            .flatMap(maquina -> validator.validate(maquina).thenReturn(maquina))
            .flatMap(maquinaRepository::save);
    }

    @Override
    public Mono<Maquina> addMantenimientoToMaquina(String maquinaId, String mantenimientoId) {
        // ✅ Operación 100% reactiva con Mono.zip
        Mono<Maquina> maquinaMono = maquinaRepository.findById(maquinaId)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(maquinaId)));

        Mono<Mantenimiento> mantenimientoMono = mantenimientoRepository.findById(mantenimientoId)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId)));

        return Mono.zip(maquinaMono, mantenimientoMono)
                .flatMap(tuple -> {
                    Maquina maquina = tuple.getT1();
                    Mantenimiento mantenimiento = tuple.getT2();
                    
                    Mantenimiento mantenimientoActualizado = mantenimiento.withMaquinaId(maquinaId);
                    Maquina maquinaActualizada = maquina.withMantenimiento(mantenimientoActualizado);

                    return mantenimientoRepository.save(mantenimientoActualizado)
                        .then(maquinaRepository.save(maquinaActualizada));
                });
    }

    @Override
    public Mono<Maquina> removeMantenimientoFromMaquina(String maquinaId, String mantenimientoId) {
        return maquinaRepository.findById(maquinaId)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(maquinaId)))
            .map(maquina -> maquina.withoutMantenimiento(mantenimientoId)) // ✅ Inmutabilidad
            .flatMap(maquinaRepository::save);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return maquinaRepository.existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(MaquinaException.notFound(id));
                }
                return maquinaRepository.deleteById(id);
            });
    }
}
