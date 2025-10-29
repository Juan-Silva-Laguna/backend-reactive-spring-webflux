//package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;
//
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//public interface RepuestoMongoRepository extends ReactiveMongoRepository<RepuestoDocument, String> {
//    
//    Mono<RepuestoDocument> findByNombre(String nombre);
//    
//    Flux<RepuestoDocument> findByPrecioBetween(Double minPrice, Double maxPrice);
//    
//    Mono<Boolean> existsByNombre(String nombre);
//    
//    // Query personalizada
//    @Query("{ 'precio': { $gte: ?0, $lte: ?1 } }")
//    Flux<RepuestoDocument> findByPriceRange(Double minPrice, Double maxPrice);
//}
