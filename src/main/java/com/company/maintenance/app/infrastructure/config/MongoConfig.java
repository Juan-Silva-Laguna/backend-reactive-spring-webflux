//package com.company.maintenance.app.infrastructure.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
//
//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//
//@Configuration
//@EnableReactiveMongoRepositories(
//    basePackages = "com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository"
//)
//public class MongoConfig extends AbstractReactiveMongoConfiguration {
//	
////	 @Bean
////	 public Mono<MongoClient> MongoConfig(Mono<String> dbSecret) {
////	        return dbSecret.map(secretJson -> {
////	            DatabaseSecret secret = SecretParser.parse(secretJson);
////	            String connectionString = String.format(
////	                    "mongodb://%s:%s@%s:%s",
////	                    secret.getUsername(),
////	                    secret.getPassword(),
////	                    secret.getHost(),
////	                    secret.getPort()
////	            );
////	            return MongoClients.create(connectionString);
////	        });
////	    }
////	}
//
//    @Value("${spring.data.mongodb.uri}")
//    private String mongoUri;
//
//    @Value("${spring.data.mongodb.database:repuestos_db}")
//    private String databaseName;
//
//    @Override
//    protected String getDatabaseName() {
//        return databaseName;
//    }
//
//    @Override
//    @Bean
//    public MongoClient reactiveMongoClient() {
//        return MongoClients.create(mongoUri);
//    }
//
//    @Bean
//    public ReactiveMongoTemplate reactiveMongoTemplate() {
//        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
//    }
//}
//
