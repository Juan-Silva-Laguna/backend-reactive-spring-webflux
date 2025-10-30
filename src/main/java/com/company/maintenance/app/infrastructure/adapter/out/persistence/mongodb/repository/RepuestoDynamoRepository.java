package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class RepuestoDynamoRepository {
    
    private final DynamoDbAsyncTable<RepuestoDocument> repuestoTable;

    public RepuestoDynamoRepository(
            DynamoDbEnhancedAsyncClient enhancedClient,
            TableSchema<RepuestoDocument> repuestoTableSchema) {
        this.repuestoTable = enhancedClient.table("Repuestos", repuestoTableSchema);
    }

    public Mono<RepuestoDocument> save(RepuestoDocument repuesto) {
        return Mono.fromFuture(
            repuestoTable.putItem(repuesto).thenApply(v -> repuesto)
        );
    }

    public Mono<RepuestoDocument> findById(String id) {
        return Mono.fromFuture(
            repuestoTable.getItem(Key.builder().partitionValue(id).build())
        );
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromFuture(
            repuestoTable.deleteItem(Key.builder().partitionValue(id).build())
        ).then();
    }

    public Flux<RepuestoDocument> findAll() {
        return Flux.from(repuestoTable.scan().items());
    }

    public Mono<RepuestoDocument> findByNombre(String nombre) {
        Expression expression = Expression.builder()
            .expression("#nombre = :nombre")
            .expressionNames(Map.of("#nombre", "nombre"))
            .expressionValues(Map.of(":nombre", AttributeValue.fromS(nombre)))
            .build();

        return Flux.from(
            repuestoTable.scan(r -> r.filterExpression(expression)).items()
        ).next();
    }

    public Flux<RepuestoDocument> findByPrecioBetween(Double minPrecio, Double maxPrecio) {
        Expression expression = Expression.builder()
            .expression("#precio BETWEEN :min AND :max")
            .expressionNames(Map.of("#precio", "precio"))
            .expressionValues(Map.of(
                ":min", AttributeValue.fromN(String.valueOf(minPrecio)),
                ":max", AttributeValue.fromN(String.valueOf(maxPrecio))
            ))
            .build();

        return Flux.from(
            repuestoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }

    public Mono<Boolean> existsById(String id) {
        return findById(id)
            .map(r -> true)
            .defaultIfEmpty(false);
    }

    public Mono<Boolean> existsByNombre(String nombre) {
        Expression expression = Expression.builder()
            .expression("#nombre = :nombre")
            .expressionNames(Map.of("#nombre", "nombre"))
            .expressionValues(Map.of(":nombre", AttributeValue.fromS(nombre)))
            .build();

        return Flux.from(
            repuestoTable.scan(r -> r.filterExpression(expression)).items()
        ).hasElements();
    }
}