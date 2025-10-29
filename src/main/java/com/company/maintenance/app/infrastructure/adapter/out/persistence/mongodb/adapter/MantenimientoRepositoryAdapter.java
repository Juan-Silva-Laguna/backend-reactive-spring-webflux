package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.adapter;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.MantenimientoDocumentMapper;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.MantenimientoDynamoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MantenimientoRepositoryAdapter implements MantenimientoRepository {

    private final MantenimientoDynamoRepository dynamoRepository;

    public MantenimientoRepositoryAdapter(MantenimientoDynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    @Override
    public Mono<Mantenimiento> save(Mantenimiento mantenimiento) {
        if (mantenimiento.getId() == null) {
        	mantenimiento.setId(UUID.randomUUID().toString());
        }
        return Mono.just(mantenimiento)
            .map(MantenimientoDocumentMapper::toDocument)
            .flatMap(dynamoRepository::save)
            .map(MantenimientoDocumentMapper::toDomain);
    }

    @Override
    public Mono<Mantenimiento> findById(String id) {
        return dynamoRepository.findById(id)
            .map(MantenimientoDocumentMapper::toDomain);
    }

    @Override
    public Flux<Mantenimiento> findAll() {
        return dynamoRepository.findAll()
            .map(MantenimientoDocumentMapper::toDomain);
    }

    @Override
    public Flux<Mantenimiento> findByFechaBetween(Date startDate, Date endDate) {
        return dynamoRepository.findByFechaBetween(startDate, endDate)
            .map(MantenimientoDocumentMapper::toDomain);
    }

    @Override
    public Flux<Mantenimiento> findByTipo(String tipo) {
        return dynamoRepository.findByTipo(tipo)
            .map(MantenimientoDocumentMapper::toDomain);
    }

    @Override
    public Flux<Mantenimiento> findByPrecioBetween(Double minPrice, Double maxPrice) {
        return dynamoRepository.findByPrecioBetween(minPrice, maxPrice)
            .map(MantenimientoDocumentMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return dynamoRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return dynamoRepository.findById(id)
            .map(m -> true)
            .defaultIfEmpty(false);
    }
}


//import java.util.Date;
//
//import org.springframework.stereotype.Component;
//
//import com.company.maintenance.app.application.port.out.MantenimientoRepository;
//import com.company.maintenance.app.domain.model.Mantenimiento;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.mapper.MantenimientoDocumentMapper;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.MantenimientoMongoRepository;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Component
//public class MantenimientoRepositoryAdapter implements MantenimientoRepository {
//
//    private final MantenimientoMongoRepository mongoRepository;
//
//    public MantenimientoRepositoryAdapter(MantenimientoMongoRepository mongoRepository) {
//        this.mongoRepository = mongoRepository;
//    }
//
//    @Override
//    public Mono<Mantenimiento> save(Mantenimiento mantenimiento) {
//        return Mono.just(mantenimiento)
//            .map(MantenimientoDocumentMapper::toDocument)
//            .flatMap(mongoRepository::save)
//            .map(MantenimientoDocumentMapper::toDomain);
//    }
//    
//    @Override
//    public Mono<Mantenimiento> findById(String id) {
//        return mongoRepository.findById(id)
//            .map(MantenimientoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Flux<Mantenimiento> findAll() {
//        return mongoRepository.findAll()
//            .map(MantenimientoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Flux<Mantenimiento> findByFechaBetween(Date startDate, Date endDate) {
//        return mongoRepository.findByFechaBetween(startDate, endDate)
//            .map(MantenimientoDocumentMapper::toDomain);
//    }
//    
//    @Override
//    public Flux<Mantenimiento> findByTipo(String tipo) {
//        return mongoRepository.findByTipo(tipo)
//            .map(MantenimientoDocumentMapper::toDomain);
//    }
//
//    @Override
//    public Flux<Mantenimiento> findByPrecioBetween(Double minPrice, Double maxPrice) {
//        return mongoRepository.findByPrecioBetween(minPrice, maxPrice)
//            .map(MantenimientoDocumentMapper::toDomain);
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
//}