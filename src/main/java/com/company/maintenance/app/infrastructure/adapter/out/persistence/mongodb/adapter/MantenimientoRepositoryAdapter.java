package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.adapter;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.MantenimientoDocumentMapper;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.MantenimientoDynamoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class MantenimientoRepositoryAdapter implements MantenimientoRepository {

    private final MantenimientoDynamoRepository dynamoRepository;
    private final MantenimientoDocumentMapper mapper;

    public MantenimientoRepositoryAdapter(
            MantenimientoDynamoRepository dynamoRepository,
            MantenimientoDocumentMapper mapper) {
        this.dynamoRepository = dynamoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Mantenimiento> save(Mantenimiento mantenimiento) {
        return Mono.fromCallable(() -> {
            if (mantenimiento.getId() == null) {
                return mantenimiento.withId(UUID.randomUUID().toString());
            }
            return mantenimiento;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(m -> Mono.fromCallable(() -> mapper.toDocument(m))
            .subscribeOn(Schedulers.boundedElastic()))
        .flatMap(dynamoRepository::save)
        .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
            .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Mantenimiento> findById(String id) {
        return dynamoRepository.findById(id)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Mantenimiento> findAll() {
        return dynamoRepository.findAll()
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Mantenimiento> findByFecha(String fecha) {
        return dynamoRepository.findByFecha(fecha)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Mantenimiento> findByFechaBetween(String startDate, String endDate) {
        return dynamoRepository.findByFechaBetween(startDate, endDate)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Mantenimiento> findByTipo(String tipo) {
        return dynamoRepository.findByTipo(tipo)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Mantenimiento> findByPrecioBetween(Double minPrecio, Double maxPrecio) {
        return dynamoRepository.findByPrecioBetween(minPrecio, maxPrecio)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return dynamoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return dynamoRepository.existsById(id);
    }
}