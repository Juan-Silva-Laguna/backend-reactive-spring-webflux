//package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;
//
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
//
//import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
//
//import org.springframework.data.mongodb.repository.Query;
//import reactor.core.publisher.Flux;
//import java.util.Date;
//
//public interface MantenimientoMongoRepository extends ReactiveMongoRepository<MantenimientoDocument, String> {
//    
//    Flux<MantenimientoDocument> findByFechaBetween(Date startDate, Date endDate);
//    
//    Flux<MantenimientoDocument> findByPrecioBetween(Double minPrice, Double maxPrice);
//    
//    Flux<MantenimientoDocument> findByTipo(String tipo);
//    
//    // Query personalizada para buscar por año
//    @Query("{ 'fecha': { $gte: ?0, $lte: ?1 } }")
//    Flux<MantenimientoDocument> findByYear(Date startOfYear, Date endOfYear);
//}