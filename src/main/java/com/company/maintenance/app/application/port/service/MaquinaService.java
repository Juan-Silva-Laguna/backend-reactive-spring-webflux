package com.company.maintenance.app.application.port.service;

import java.util.List;

import com.company.maintenance.app.application.port.in.MaquinaUseCase;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.domain.exception.MaquinaException;
import com.company.maintenance.app.domain.exception.RepuestoException;
import com.company.maintenance.app.domain.exception.MantenimientoException;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.domain.model.Mantenimiento;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MaquinaService implements MaquinaUseCase{

	private final MaquinaRepository maquinaRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public MaquinaService(MaquinaRepository maquinaRepository,
                               MantenimientoRepository mantenimientoRepository) {
        this.maquinaRepository = maquinaRepository;
        this.mantenimientoRepository = mantenimientoRepository;
    }

    @Override
    public Mono<Maquina> createMaquina(String nombre, String modelo, List<String> mantenimientosIds) {
        
//        return maquinaRepository.existsByNombre(nombre)
//                .flatMap(exists -> {
//                    if (exists) {
//                        return Mono.error(RepuestoException.alreadyExists(nombre));
//                    }
//                    Maquina maquina = new Maquina(nombre, modelo);
//                    return maquinaRepository.save(maquina);
//                });

            
            if (mantenimientosIds == null || mantenimientosIds.isEmpty()) {
            	Maquina maquina = new Maquina(nombre, modelo, null);
                return maquinaRepository.save(maquina);
            }

            return Flux.fromIterable(mantenimientosIds)
                .flatMap(id -> mantenimientoRepository.findById(id)
                    .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
                )
                .collectList()
                .map(mantenimientos -> {
                	Maquina maquina = new Maquina(nombre, modelo, mantenimientos);
                    return maquina;
                })
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
            		MaquinaException.notFound("No se encontró maquina con nombre: " + nombre)
            ));
    }

    @Override
    public Mono<Maquina> updateMaquina(String id, String nombre, String modelo) {
        return maquinaRepository.findById(id)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(id)))
            .map(maquina -> {
                maquina.setNombre(nombre);
                maquina.setModelo(modelo);
                return maquina;
            })
            .flatMap(maquinaRepository::save);
    }

    @Override
    public Mono<Maquina> addMantenimientoToMaquina(String maquinaId, String mantenimientoId) {
        return Mono.zip(
            maquinaRepository.findById(maquinaId)
                .switchIfEmpty(Mono.error(MaquinaException.notFound(maquinaId))),
            mantenimientoRepository.findById(mantenimientoId)
                .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId)))
        )
        .map(tuple -> {
            Maquina maquina = tuple.getT1();
            Mantenimiento mantenimiento = tuple.getT2();
            maquina.addMantenimiento(mantenimiento);
            return maquina;
        })
        .flatMap(maquinaRepository::save);
    }

    @Override
    public Mono<Maquina> removeMantenimientoFromMaquina(String maquinaId, String mantenimientoId) {
        return maquinaRepository.findById(maquinaId)
            .switchIfEmpty(Mono.error( MaquinaException.notFound(maquinaId)))
            .map(maquina -> {
                maquina.removeMantenimiento(mantenimientoId);
                return maquina;
            })
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