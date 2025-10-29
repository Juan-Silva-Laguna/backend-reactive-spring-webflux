package com.company.maintenance.app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MantenimientoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.MaquinaDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoConfig {

    @Primary
    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() {

        return DynamoDbAsyncClient.builder()
                .region(Region.US_EAST_1) 
                .build();
    }
    
    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient client) {
        return DynamoDbEnhancedAsyncClient.builder()
            .dynamoDbClient(client)
            .build();
    }
    
    @Bean
    public TableSchema<MaquinaDocument> maquinaTableSchema() {
        return TableSchema.fromBean(MaquinaDocument.class);
    }
    
    @Bean
    public TableSchema<RepuestoDocument> repuestoTableSchema() {
        return TableSchema.fromBean(RepuestoDocument.class);
    }
    
    @Bean
    public TableSchema<MantenimientoDocument> manteniemientoTableSchema() {
        return TableSchema.fromBean(MantenimientoDocument.class);
    }


}
