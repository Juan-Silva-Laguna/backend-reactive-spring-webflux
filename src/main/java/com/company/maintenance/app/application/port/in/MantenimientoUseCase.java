package com.company.maintenance.app.application.port.in;

import com.company.maintenance.app.domain.model.Mantenimiento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface MantenimientoUseCase {
    Mono<Mantenimiento> createMantenimiento(String fecha, String descripcion, 
                                           Double precio, List<String> repuestosIds, String tipo);
    
    Mono<Mantenimiento> createMantenimientoAsignaMaquina(String maquinaId, String fecha, 
                                                         String descripcion, Double precio, 
                                                         List<String> repuestosIds, String tipo);
    
    Mono<Mantenimiento> findById(String id);
    Flux<Mantenimiento> findAll();
    Flux<Mantenimiento> findByFecha(String fecha);
    Flux<Mantenimiento> findByFechaRange(String startDate, String endDate);
    Flux<Mantenimiento> findByTipo(String tipo);
    Flux<Mantenimiento> findByPriceRange(Double minPrecio, Double maxPrecio);
    
    Mono<Mantenimiento> updateMantenimiento(String id, String fecha, String descripcion, 
                                           Double precio, String tipo);
    
    Mono<Mantenimiento> addRepuestoToMantenimiento(String mantenimientoId, String repuestoId);
    Mono<Mantenimiento> removeRepuestoFromMantenimiento(String mantenimientoId, String repuestoId);
    Mono<Void> deleteById(String id);
}
