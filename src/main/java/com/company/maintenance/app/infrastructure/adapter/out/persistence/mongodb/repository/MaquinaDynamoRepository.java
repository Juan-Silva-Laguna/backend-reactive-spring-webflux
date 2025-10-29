package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MaquinaDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class MaquinaDynamoRepository {
    
    private final DynamoDbAsyncTable<MaquinaDocument> maquinaTable;

    public MaquinaDynamoRepository(DynamoDbEnhancedAsyncClient enhancedClient, TableSchema<MaquinaDocument> maquinaTableSchema) {
        this.maquinaTable = enhancedClient.table("Maquinas", maquinaTableSchema);
    }

    public Mono<MaquinaDocument> save(MaquinaDocument maquina) {
        return Mono.fromFuture(maquinaTable.putItem(maquina).thenApply(v -> maquina));
    }

    public Mono<MaquinaDocument> findById(String id) {
        return Mono.fromFuture(
            maquinaTable.getItem(Key.builder().partitionValue(id).build())
        );
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromFuture(
            maquinaTable.deleteItem(Key.builder().partitionValue(id).build())
        ).then();
    }

    public Flux<MaquinaDocument> findAll() {
        return Flux.from(maquinaTable.scan().items());
    }

    public Mono<MaquinaDocument> findByNombre(String nombre) {
        Expression expression = Expression.builder()
            .expression("#nombre = :nombre")
            .expressionNames(Map.of("#nombre", "nombre"))
            .expressionValues(Map.of(":nombre", AttributeValue.fromS(nombre)))
            .build();

        return Flux.from(
            maquinaTable.scan(r -> r.filterExpression(expression)).items()
        ).next(); // devolvemos solo el primero
    }

    public Mono<Boolean> existsById(String id) {
        return findById(id)
            .map(m -> true)
            .defaultIfEmpty(false);
    }

    public Mono<Boolean> existsByNombre(String nombre) {
        Expression expression = Expression.builder()
            .expression("#nombre = :nombre")
            .expressionNames(Map.of("#nombre", "nombre"))
            .expressionValues(Map.of(":nombre", AttributeValue.fromS(nombre)))
            .build();

        return Flux.from(
            maquinaTable.scan(r -> r.filterExpression(expression)).items()
        ).hasElements();
    }
}
