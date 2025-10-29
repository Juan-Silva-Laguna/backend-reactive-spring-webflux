package com.company.maintenance.app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.application.port.out.RepuestoRepository;
import com.company.maintenance.app.application.port.service.MantenimientoService;
import com.company.maintenance.app.application.port.service.MaquinaService;
import com.company.maintenance.app.application.port.service.RepuestoService;
import com.company.maintenance.app.application.port.in.MantenimientoUseCase;
import com.company.maintenance.app.application.port.in.MaquinaUseCase;
import com.company.maintenance.app.application.port.in.RepuestoUseCase;


@Configuration
public class BeanConfiguration {

    @Bean
    public RepuestoUseCase repuestoUseCase(RepuestoRepository repuestoRepository) {
        return new RepuestoService(repuestoRepository);
    }

    @Bean
    public MantenimientoUseCase mantenimientoUseCase(MantenimientoRepository mantenimientoRepository, RepuestoRepository repuestoRepository) {
        return new MantenimientoService(mantenimientoRepository, repuestoRepository);
    }
    
    @Bean
    public MaquinaUseCase maquinaUseCase(MaquinaRepository maquinaRepository, MantenimientoRepository mantenimientoRepository) {
        return new MaquinaService(maquinaRepository, mantenimientoRepository);
    }
}

//@Configuration
//public class BeanConfiguration {
//    
//    @Bean
//    @Primary
//    public RepuestoService repuestoService(RepuestoRepository repuestoRepository) {
//        return new RepuestoService(repuestoRepository);
//    }
//    
//    @Bean
//    public RepuestoUseCase repuestoUseCase(RepuestoService repuestoService) {
//        return repuestoService;
//    }
//    
//    @Bean
//    @Primary
//    public MantenimientoService mantenimientoService(MantenimientoRepository mantenimientoRepository, RepuestoRepository repuestoRepository) {
//        return new MantenimientoService(mantenimientoRepository, repuestoRepository);
//    }
//    
//    @Bean
//    public MantenimientoUseCase mantenimientoUseCase(MantenimientoService mantenimientoService) {
//        return mantenimientoService;
//    }
//    
//}