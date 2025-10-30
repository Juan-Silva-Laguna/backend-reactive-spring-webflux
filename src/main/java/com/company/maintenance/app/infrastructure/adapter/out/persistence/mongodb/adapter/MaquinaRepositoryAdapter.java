package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.adapter;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.MaquinaDocumentMapper;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.MaquinaDynamoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class MaquinaRepositoryAdapter implements MaquinaRepository {

    private final MaquinaDynamoRepository dynamoRepository;
    private final MaquinaDocumentMapper mapper; // ✅ Inyectado

    public MaquinaRepositoryAdapter(
            MaquinaDynamoRepository dynamoRepository,
            MaquinaDocumentMapper mapper) {
        this.dynamoRepository = dynamoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Maquina> save(Maquina maquina) {
        return Mono.fromCallable(() -> {
            if (maquina.getId() == null) {
                return maquina.withId(UUID.randomUUID().toString()); // ✅ Inmutable
            }
            return maquina;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(m -> Mono.fromCallable(() -> mapper.toDocument(m))
            .subscribeOn(Schedulers.boundedElastic()))
        .flatMap(dynamoRepository::save)
        .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
            .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Maquina> findById(String id) {
        return dynamoRepository.findById(id)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<Maquina> findByNombre(String nombre) {
        return dynamoRepository.findByNombre(nombre)
            .flatMap(doc -> Mono.fromCallable(() -> mapper.toDomain(doc))
                .subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Flux<Maquina> findAll() {
        return dynamoRepository.findAll()
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