package com.company.maintenance.app.infrastructure.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.company.maintenance.app.domain.validator.CompositeMaquinaValidator;
import com.company.maintenance.app.domain.validator.MaquinaValidator;
import com.company.maintenance.app.domain.validator.ModeloNotEmptyValidator;
import com.company.maintenance.app.domain.validator.NombreNotNullValidator;

@Configuration
public class ValidatorConfig {

    @Bean
    public MaquinaValidator maquinaValidator() {
        return new CompositeMaquinaValidator(List.of(
            new NombreNotNullValidator(),
            new ModeloNotEmptyValidator()
        ));
    }
}