package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;


import org.springframework.stereotype.Repository;

import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.Map;

@Repository
public class RepuestoDynamoRepository {

	private final DynamoDbAsyncTable<RepuestoDocument> repuestoTable;

    public RepuestoDynamoRepository(DynamoDbEnhancedAsyncClient enhancedClient,
                                    TableSchema<RepuestoDocument> repuestoTableSchema) {
        this.repuestoTable = enhancedClient.table("Repuestos", repuestoTableSchema);
    }

    public Mono<RepuestoDocument> save(RepuestoDocument repuesto) {
        return Mono.fromFuture(() -> repuestoTable.putItem(repuesto))
                   .then(Mono.just(repuesto));
    }

    public Mono<RepuestoDocument> findById(String id) {
        return Mono.fromFuture(() -> repuestoTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }
    

    public Mono<Void> deleteById(String id) {
        return Mono.fromFuture(repuestoTable.deleteItem(Key.builder().partitionValue(id).build())).then();
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

        SdkPublisher<Page<RepuestoDocument>> publisher = repuestoTable.scan(r -> r.filterExpression(expression));

        return Flux.from(publisher)
            .flatMapIterable(Page::items)
            .next(); // Devuelve solo el primero
    }

    public Flux<RepuestoDocument> findByPrecioBetween(Double minPrice, Double maxPrice) {


        Expression expression = Expression.builder()
            .expression("#precio BETWEEN :min AND :max")
            .expressionNames(Map.of("#precio", "precio"))
            .expressionValues(Map.of(
                ":min", AttributeValue.fromN(minPrice.toString()),
                ":max", AttributeValue.fromN(maxPrice.toString())
            ))
            .build();

        return Flux.from(repuestoTable.scan(r -> r.filterExpression(expression)).items());
    }

    public Mono<Boolean> existsByNombre(String nombre) {


        Expression expression = Expression.builder()
            .expression("#nombre = :nombre")
            .expressionNames(Map.of("#nombre", "nombre"))
            .expressionValues(Map.of(":nombre", AttributeValue.fromS(nombre)))
            .build();

        SdkPublisher<Page<RepuestoDocument>> publisher = repuestoTable.scan(r -> r.filterExpression(expression));

        return Flux.from(publisher)
            .flatMapIterable(Page::items)
            .hasElements();
    }

    public Mono<Boolean> existsById(String id) {


        return findById(id)
            .map(item -> true)
            .defaultIfEmpty(false);
    }
}