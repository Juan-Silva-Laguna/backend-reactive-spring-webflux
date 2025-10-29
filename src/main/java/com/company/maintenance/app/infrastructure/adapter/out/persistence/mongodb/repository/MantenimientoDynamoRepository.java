package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class MantenimientoDynamoRepository {

	private final DynamoDbAsyncTable<MantenimientoDocument> mantenimientoTable;

    public MantenimientoDynamoRepository(DynamoDbEnhancedAsyncClient enhancedClient,
                                    TableSchema<MantenimientoDocument> mantenimientoTableSchema) {
        this.mantenimientoTable = enhancedClient.table("Mantenimientos", mantenimientoTableSchema);
    }

    private static final SimpleDateFormat formatoISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


    
    public Mono<MantenimientoDocument> save(MantenimientoDocument mantenimiento) {
    	   return Mono.fromFuture(() -> mantenimientoTable.putItem(mantenimiento))
                   .then(Mono.just(mantenimiento));
//        return Mono.fromFuture(mantenimientoTable.putItem(mantenimiento).thenApply(v -> mantenimiento));
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

    public Flux<MantenimientoDocument> findByFechaBetween(Date start, Date end) {
        Expression expression = Expression.builder()
            .expression("#fecha BETWEEN :start AND :end")
            .expressionNames(Map.of("#fecha", "fecha"))
            .expressionValues(Map.of(
        	    ":start", AttributeValue.fromS(formatoISO.format(start)),
        	    ":end", AttributeValue.fromS(formatoISO.format(end))
        	))
            .build();

        return Flux.from(
            mantenimientoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }
    
    public Flux<MantenimientoDocument> findByPrecioBetween(Double min, Double max) {
        Expression expression = Expression.builder()
            .expression("#precio BETWEEN :min AND :max")
            .expressionNames(Map.of("#precio", "precio"))
            .expressionValues(Map.of(
                ":min", AttributeValue.fromN(min.toString()),
                ":max", AttributeValue.fromN(max.toString())
            ))
            .build();

        return Flux.from(
            mantenimientoTable.scan(r -> r.filterExpression(expression)).items()
        );
    }

}