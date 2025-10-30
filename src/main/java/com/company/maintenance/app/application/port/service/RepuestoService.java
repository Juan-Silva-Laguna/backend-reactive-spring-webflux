package com.company.maintenance.app.application.port.service;

import org.springframework.stereotype.Service;
import com.company.maintenance.app.application.port.in.RepuestoUseCase;
import com.company.maintenance.app.application.port.out.RepuestoRepository;
import com.company.maintenance.app.domain.exception.RepuestoException;
import com.company.maintenance.app.domain.model.Repuesto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RepuestoService implements RepuestoUseCase {

    private final RepuestoRepository repuestoRepository;

    public RepuestoService(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    @Override
    public Mono<Repuesto> createRepuesto(String nombre, Double precio) {
        return repuestoRepository.existsByNombre(nombre)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(RepuestoException.alreadyExists(nombre));
                }
                Repuesto repuesto = new Repuesto(nombre, precio);
                return repuestoRepository.save(repuesto);
            });
    }

    @Override
    public Mono<Repuesto> findById(String id) {
        return repuestoRepository.findById(id)
            .switchIfEmpty(Mono.error(RepuestoException.notFound(id)));
    }

    @Override
    public Mono<Repuesto> findByNombre(String nombre) {
        return repuestoRepository.findByNombre(nombre)
            .switchIfEmpty(Mono.error(
                RepuestoException.notFound("No se encontró repuesto con nombre: " + nombre)
            ));
    }

    @Override
    public Flux<Repuesto> findAll() {
        return repuestoRepository.findAll();
    }

    @Override
    public Flux<Repuesto> findByPriceRange(Double minPrecio, Double maxPrecio) {
        return repuestoRepository.findByPrecioBetween(minPrecio, maxPrecio);
    }

    @Override
    public Mono<Repuesto> updateRepuesto(String id, String nombre, Double precio) {
        return repuestoRepository.findById(id)
            .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
            .map(repuesto -> repuesto
                .withNombre(nombre)
                .withPrecio(precio)
            )
            .flatMap(repuestoRepository::save);
    }

    @Override
    public Mono<Repuesto> updatePrice(String id, Double precio) {
        return repuestoRepository.findById(id)
            .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
            .map(repuesto -> repuesto.withPrecio(precio))
            .flatMap(repuestoRepository::save);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repuestoRepository.existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(RepuestoException.notFound(id));
                }
                return repuestoRepository.deleteById(id);
            });
    }
}