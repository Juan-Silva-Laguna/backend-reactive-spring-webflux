package com.company.maintenance.app.infrastructure.utils;

import com.company.maintenance.app.domain.model.DatabaseSecret;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SecretParser {

    public static DatabaseSecret parse(String jsonSecret) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonSecret, DatabaseSecret.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing AWS secret JSON", e);
        }
    }
}