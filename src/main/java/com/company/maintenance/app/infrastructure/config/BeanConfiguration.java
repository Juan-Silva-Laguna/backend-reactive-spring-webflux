package com.company.maintenance.app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.company.maintenance.app.application.port.in.MaquinaUseCase;
import com.company.maintenance.app.application.port.in.MantenimientoUseCase;
import com.company.maintenance.app.application.port.in.RepuestoUseCase;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.application.port.out.MantenimientoReadRepository;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.application.port.out.RepuestoRepository;
import com.company.maintenance.app.application.port.out.RepuestoReadRepository;
import com.company.maintenance.app.application.port.service.MaquinaService;
import com.company.maintenance.app.application.port.service.MantenimientoService;
import com.company.maintenance.app.application.port.service.RepuestoService;
import com.company.maintenance.app.domain.validator.MaquinaValidator;

@Configuration
public class BeanConfiguration {

	@Bean
	public MaquinaUseCase maquinaUseCase(
	        MaquinaRepository maquinaRepository,
	        MantenimientoRepository mantenimientoRepository,
	        MaquinaValidator maquinaValidator) {
	    return new MaquinaService(maquinaRepository, mantenimientoRepository, maquinaValidator);
	}


    @Bean
    public MantenimientoUseCase mantenimientoUseCase(
            MantenimientoRepository mantenimientoRepository,
            MaquinaRepository maquinaRepository,
            RepuestoReadRepository repuestoRepository) { // ✅ Tres parámetros
        return new MantenimientoService(
            mantenimientoRepository, 
            maquinaRepository, 
            repuestoRepository
        );
    }

    @Bean
    public RepuestoUseCase repuestoUseCase(RepuestoRepository repuestoRepository) {
        return new RepuestoService(repuestoRepository);
    }
}
