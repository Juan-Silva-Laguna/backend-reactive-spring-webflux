package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.domain.model.Maquina;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.MaquinaDocumentMapper;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.MaquinaDynamoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MaquinaRepositoryAdapter implements MaquinaRepository {

    private final MaquinaDynamoRepository dynamoRepository;

    public MaquinaRepositoryAdapter(MaquinaDynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    @Override
    public Mono<Maquina> save(Maquina maquina) {
        if (maquina.getId() == null) {
        	maquina.setId(UUID.randomUUID().toString());
        }
        return Mono.just(maquina)
            .map(MaquinaDocumentMapper::toDocument)
            .flatMap(dynamoRepository::save)
            .map(MaquinaDocumentMapper::toDomain);
    }

    @Override
    public Mono<Maquina> findById(String id) {
        return dynamoRepository.findById(id)
            .map(MaquinaDocumentMapper::toDomain);
    }

    @Override
    public Mono<Maquina> findByNombre(String nombre) {
        return dynamoRepository.findByNombre(nombre)
            .map(MaquinaDocumentMapper::toDomain); 
    }

    @Override
    public Flux<Maquina> findAll() {
        return dynamoRepository.findAll()
            .map(MaquinaDocumentMapper::toDomain);
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

//import org.springframework.stereotype.Component;
//
//import com.company.maintenance.app.application.port.out.MaquinaRepository;
//import com.company.maintenance.app.domain.model.Maquina;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.MaquinaDocumentMapper;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.MaquinaMongoRepository;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Component
//public class MaquinaRepositoryAdapter implements MaquinaRepository {
//
//    private final MaquinaMongoRepository mongoRepository;
//
//    public MaquinaRepositoryAdapter(MaquinaMongoRepository mongoRepository) {
//        this.mongoRepository = mongoRepository;
//    }
//
//    @Override
//    public Mono<Maquina> save(Maquina maquina) {
//        return Mono.just(maquina)
//            .map(MaquinaDocumentMapper::toDocument)
//            .flatMap(mongoRepository::save)
//            .map(MaquinaDocumentMapper::toDomain);
//    }
//    
//    @Override
//    public Mono<Maquina> findById(String id) {
//        return mongoRepository.findById(id)
//            .map(MaquinaDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Mono<Maquina> findByNombre(String nombre) {
//        return mongoRepository.findByNombre(nombre)
//            .map(MaquinaDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Flux<Maquina> findAll() {
//        return mongoRepository.findAll()
//            .map(MaquinaDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Mono<Void> deleteById(String id) {
//        return mongoRepository.deleteById(id);
//    }
//
//    @Override
//    public Mono<Boolean> existsById(String id) {
//        return mongoRepository.existsById(id);
//    }
//    
//    @Override
//    public Mono<Boolean> existsByNombre(String nombre) {
//        return mongoRepository.existsByNombre(nombre);
//    }
//}