package com.company.maintenance.app.application.port.in;

import java.util.Date;
import java.util.List;

import com.company.maintenance.app.domain.model.Mantenimiento;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MantenimientoUseCase {
    Mono<Mantenimiento> createMantenimiento(Date fecha, String descripcion, Double precio, List<String> repuestosIds, String tipo);
    Mono<Mantenimiento> findById(String id);
    Flux<Mantenimiento> findAll();
    Flux<Mantenimiento> findByTipo(String tipo);
    Flux<Mantenimiento> findByFechaRange(Date startDate, Date endDate);
    Flux<Mantenimiento> findByPriceRange(Double minPrice, Double maxPrice);
    Mono<Mantenimiento> updateMantenimiento(String id, Date fecha, String descripcion, Double precio, String tipo);
    Mono<Mantenimiento> addRepuestoToMantenimiento(String mantenimientoId, String repuestoId);
    Mono<Mantenimiento> removeRepuestoFromMantenimiento(String mantenimientoId, String repuestoId);
    Mono<Void> deleteById(String id);
    Mono<Mantenimiento> createMantenimientoAsignaMaquina(Date fecha, String descripcion, Double precio, List<String> repuestosIds, String tipo, String idMaquina);
}
