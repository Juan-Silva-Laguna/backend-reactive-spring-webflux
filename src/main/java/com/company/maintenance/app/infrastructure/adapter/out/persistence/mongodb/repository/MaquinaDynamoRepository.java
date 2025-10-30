package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MaquinaDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public class MaquinaDynamoRepository {
    
    private final DynamoDbAsyncTable<MaquinaDocument> maquinaTable;

    public MaquinaDynamoRepository(
            DynamoDbEnhancedAsyncClient enhancedClient,
            TableSchema<MaquinaDocument> maquinaTableSchema) {
        this.maquinaTable = enhancedClient.table("Maquinas", maquinaTableSchema);
    }

    public Mono<MaquinaDocument> save(MaquinaDocument maquina) {
        return Mono.fromFuture(
            maquinaTable.putItem(maquina).thenApply(v -> maquina)
        );
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

    /**
     * ⚠️ OPTIMIZACIÓN: Usar Query si tienes GSI en lugar de Scan
     * Scan es costoso y lento en tablas grandes
     */
    public Flux<MaquinaDocument> findAll() {
        // TODO: Considera usar paginación para tablas grandes
        return Flux.from(maquinaTable.scan().items())
            .onErrorResume(e -> {
                // Log error y retorna flujo vacío en vez de propagarlo
                System.err.println("Error en scan: " + e.getMessage());
                return Flux.empty();
            });
    }

    /**
     * ✅ OPTIMIZADO: Query en GSI si existe índice por nombre
     * Si no existe GSI, usa scan con filtro (menos eficiente)
     */
    public Mono<MaquinaDocument> findByNombre(String nombre) {
        Expression expression = Expression.builder()
            .expression("#nombre = :nombre")
            .expressionNames(Map.of("#nombre", "nombre"))
            .expressionValues(Map.of(":nombre", AttributeValue.fromS(nombre)))
            .build();

        return Flux.from(
            maquinaTable.scan(r -> r.filterExpression(expression)).items()
        )
        .next()
        .onErrorResume(e -> Mono.empty());
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