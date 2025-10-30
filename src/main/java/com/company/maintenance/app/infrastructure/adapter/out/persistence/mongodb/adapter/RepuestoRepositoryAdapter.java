package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.adapter;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.company.maintenance.app.application.port.out.RepuestoRepository;
import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.RepuestoDocumentMapper;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.RepuestoDynamoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class RepuestoRepositoryAdapter implements RepuestoRepository {

    private final RepuestoDynamoRepository dynamoRepository;
    private final RepuestoDocumentMapper mapper;

    public RepuestoRepositoryAdapter(
            RepuestoDynamoRepository dynamoRepository,
            RepuestoDocumentMapper mapper) {
        this.dynamoRepository = dynamoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Repuesto> save(Repuesto repuesto) {
        return Mono.fromCallable(() -> {
            if (repuesto.getId() == null) {
                return repuesto.withId(UUID.randomUUID().toString());
            }
            return repuesto;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(r -> Mono.fromCallable(() -> mapper.toDocument(r))
            .subscribeOn(Schedulers.boundedElastic()))
        .flatMap(dynamoRepository::save)
        .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
            .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Repuesto> findById(String id) {
        return dynamoRepository.findById(id)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Repuesto> findByNombre(String nombre) {
        return dynamoRepository.findByNombre(nombre)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Repuesto> findAll() {
        return dynamoRepository.findAll()
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Repuesto> findByPrecioBetween(Double minPrecio, Double maxPrecio) {
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

    @Override
    public Mono<Boolean> existsByNombre(String nombre) {
        return dynamoRepository.existsByNombre(nombre);
    }
}