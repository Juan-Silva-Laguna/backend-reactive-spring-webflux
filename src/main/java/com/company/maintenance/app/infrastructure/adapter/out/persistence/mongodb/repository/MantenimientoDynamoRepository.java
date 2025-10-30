package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class MantenimientoDynamoRepository {
    
    private final DynamoDbAsyncTable<MantenimientoDocument> mantenimientoTable;

    public MantenimientoDynamoRepository(
            DynamoDbEnhancedAsyncClient enhancedClient,
            TableSchema<MantenimientoDocument> mantenimientoTableSchema) {
        this.mantenimientoTable = enhancedClient.table("Mantenimientos", mantenimientoTableSchema);
    }

    public Mono<MantenimientoDocument> save(MantenimientoDocument mantenimiento) {
        return Mono.fromFuture(
            mantenimientoTable.putItem(mantenimiento).thenApply(v -> mantenimiento)
        );
    }

    public Mono<MantenimientoDocument> findById(String id) {
        return Mono.fromFuture(
            mantenimientoTable.getItem(Key.builder().partitionValue(id).build())
        );
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromFuture(
            mantenimientoTable.deleteItem(Key.builder().partitionValue(id).build())
        ).then();
    }

    public Flux<MantenimientoDocument> findAll() {
        return Flux.from(mantenimientoTable.scan().items());
    }

    public Flux<MantenimientoDocument> findByFecha(String fecha) {
        Expression expression = Expression.builder()
            .expression("#fecha = :fecha")
            .expressionNames(Map.of("#fecha", "fecha"))
            .expressionValues(Map.of(":fecha", AttributeValue.fromS(fecha)))
            .build();

        return Flux.from(
            mantenimientoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }

    public Flux<MantenimientoDocument> findByFechaBetween(String startDate, String endDate) {
        Expression expression = Expression.builder()
            .expression("#fecha BETWEEN :start AND :end")
            .expressionNames(Map.of("#fecha", "fecha"))
            .expressionValues(Map.of(
                ":start", AttributeValue.fromS(startDate),
                ":end", AttributeValue.fromS(endDate)
            ))
            .build();

        return Flux.from(
            mantenimientoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }

    public Flux<MantenimientoDocument> findByTipo(String tipo) {
        Expression expression = Expression.builder()
            .expression("#tipo = :tipo")
            .expressionNames(Map.of("#tipo", "tipo"))
            .expressionValues(Map.of(":tipo", AttributeValue.fromS(tipo)))
            .build();

        return Flux.from(
            mantenimientoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }

    public Flux<MantenimientoDocument> findByPrecioBetween(Double minPrecio, Double maxPrecio) {
        Expression expression = Expression.builder()
            .expression("#precio BETWEEN :min AND :max")
            .expressionNames(Map.of("#precio", "precio"))
            .expressionValues(Map.of(
                ":min", AttributeValue.fromN(String.valueOf(minPrecio)),
                ":max", AttributeValue.fromN(String.valueOf(maxPrecio))
            ))
            .build();

        return Flux.from(
            mantenimientoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }

    public Mono<Boolean> existsById(String id) {
        return findById(id)
            .map(m -> true)
            .defaultIfEmpty(false);
    }
}
