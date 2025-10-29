package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.company.maintenance.app.application.port.out.RepuestoRepository;
import com.company.maintenance.app.domain.model.Repuesto;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.RepuestoDocumentMapper;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.RepuestoDynamoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RepuestoRepositoryAdapter implements RepuestoRepository {

    private final RepuestoDynamoRepository dynamoRepository;

    public RepuestoRepositoryAdapter(RepuestoDynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    @Override
    public Mono<Repuesto> save(Repuesto repuesto) {
        if (repuesto.getId() == null) {
            repuesto.setId(UUID.randomUUID().toString());
        }
        RepuestoDocument doc = RepuestoDocumentMapper.toDocument(repuesto);
        return dynamoRepository.save(doc)
                               .map(RepuestoDocumentMapper::toDomain);
    }


    @Override
    public Mono<Repuesto> findById(String id) {
        return dynamoRepository.findById(id)
        	    .doOnNext(d -> System.out.println("Desde DynamoDB: " + d))

            .map(RepuestoDocumentMapper::toDomain);
    }

    @Override
    public Mono<Repuesto> findByNombre(String nombre) {
        return dynamoRepository.findByNombre(nombre)
            .map(RepuestoDocumentMapper::toDomain);
    }

    @Override
    public Flux<Repuesto> findAll() {
        return dynamoRepository.findAll()
            .map(RepuestoDocumentMapper::toDomain);
    }

    @Override
    public Flux<Repuesto> findByPrecioBetween(Double minPrice, Double maxPrice) {
        return dynamoRepository.findByPrecioBetween(minPrice, maxPrice)
            .map(RepuestoDocumentMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return dynamoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsByNombre(String nombre) {
        return dynamoRepository.existsByNombre(nombre);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return dynamoRepository.existsById(id);
    }
}

//import org.springframework.stereotype.Component;
//
//import com.company.maintenance.app.application.port.out.RepuestoRepository;
//import com.company.maintenance.app.domain.model.Repuesto;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.RepuestoDocumentMapper;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.RepuestoMongoRepository;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Component
//public class RepuestoRepositoryAdapter implements RepuestoRepository {
//    
//    private final RepuestoMongoRepository mongoRepository;
//
//    public RepuestoRepositoryAdapter(RepuestoMongoRepository mongoRepository) {
//        this.mongoRepository = mongoRepository;
//    }
//
//    @Override
//    public Mono<Repuesto> save(Repuesto repuesto) {
//        return Mono.just(repuesto)
//            .map(RepuestoDocumentMapper::toDocument)
//            .flatMap(mongoRepository::save)
//            .map(RepuestoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Mono<Repuesto> findById(String id) {
//        return mongoRepository.findById(id)
//            .map(RepuestoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Mono<Repuesto> findByNombre(String nombre) {
//        return mongoRepository.findByNombre(nombre)
//            .map(RepuestoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Flux<Repuesto> findAll() {
//        return mongoRepository.findAll()
//            .map(RepuestoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Flux<Repuesto> findByPrecioBetween(Double minPrice, Double maxPrice) {
//        return mongoRepository.findByPrecioBetween(minPrice, maxPrice)
//            .map(RepuestoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Mono<Void> deleteById(String id) {
//        return mongoRepository.deleteById(id);
//    }
//
//    @Override
//    public Mono<Boolean> existsByNombre(String nombre) {
//        return mongoRepository.existsByNombre(nombre);
//    }
//
//    @Override
//    public Mono<Boolean> existsById(String id) {
//        return mongoRepository.existsById(id);
//    }
//}
