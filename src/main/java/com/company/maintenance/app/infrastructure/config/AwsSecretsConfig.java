package com.company.maintenance.app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class AwsSecretsConfig {

    private static final String SECRET_ID = "maintenance-db-credentials";
    private static final Region REGION = Region.US_EAST_1;

    @Bean
    public SecretsManagerAsyncClient secretsManagerClient() {
        return SecretsManagerAsyncClient.builder()
                .region(REGION)
                .build();
    }

    @Bean
    public Mono<String> dbSecret(SecretsManagerAsyncClient client) {
        return Mono.fromFuture(
                client.getSecretValue(GetSecretValueRequest.builder()
                        .secretId(SECRET_ID)
                        .build())
            )
            .map(GetSecretValueResponse::secretString);
    }

}