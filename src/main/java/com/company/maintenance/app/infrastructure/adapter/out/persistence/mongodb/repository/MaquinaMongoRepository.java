//package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;
//
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MaquinaDocument;
//import reactor.core.publisher.Mono;
//
//public interface MaquinaMongoRepository extends ReactiveMongoRepository<MaquinaDocument, String> {
//    
//	Mono<MaquinaDocument> findByNombre(String nombre);
//	
//    Mono<Boolean> existsByNombre(String nombre);
//
//}