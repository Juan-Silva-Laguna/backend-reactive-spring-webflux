package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.company.maintenance.app.application.port.in.MantenimientoUseCase;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.AddRepuestoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.mapper.MantenimientoDtoMapper;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/mantenimientos")
@PreAuthorize("hasAnyRole('TECNICO', 'SUPERVISOR')")
public class MantenimientoController {
    
    private final MantenimientoUseCase mantenimientoUseCase;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public MantenimientoController(MantenimientoUseCase mantenimientoUseCase) {
        this.mantenimientoUseCase = mantenimientoUseCase;
    }

    /**
     * ✅ Convertir Date a String antes de llamar al use case
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MantenimientoResponse> createMantenimiento(
            @Valid @RequestBody MantenimientoRequest request) {
        
        String fechaStr = dateFormat.format(request.getFecha()); // ✅ Conversión
        
        return mantenimientoUseCase.createMantenimiento(
                fechaStr,
                request.getDescripcion(),
                request.getPrecio(),
                request.getRepuestosIds(),
                request.getTipo()
            )
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Crear mantenimiento asignado a máquina
     */
    @PostMapping("/maquina/{maquinaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MantenimientoResponse> createMantenimientoForMaquina(
            @PathVariable String maquinaId,
            @Valid @RequestBody MantenimientoRequest request) {
        
        String fechaStr = dateFormat.format(request.getFecha()); // ✅ Conversión
        
        return mantenimientoUseCase.createMantenimientoAsignaMaquina(
                maquinaId,
                fechaStr,
                request.getDescripcion(),
                request.getPrecio(),
                request.getRepuestosIds(),
                request.getTipo()
            )
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    @GetMapping("/{id}")
    public Mono<MantenimientoResponse> getMantenimientoById(@PathVariable String id) {
        return mantenimientoUseCase.findById(id)
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    @GetMapping
    public Flux<MantenimientoResponse> getAllMantenimientos() {
        return mantenimientoUseCase.findAll()
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Búsqueda por rango de fechas
     */
    @GetMapping("/fecha-range")
    public Flux<MantenimientoResponse> getMantenimientosByFechaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return mantenimientoUseCase.findByFechaRange(startDateStr, endDateStr)
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }


    @GetMapping("/tipo/{tipo}")
    public Flux<MantenimientoResponse> getMantenimientosByTipo(@PathVariable String tipo) {
        return mantenimientoUseCase.findByTipo(tipo)
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    @GetMapping("/precio-range")
    public Flux<MantenimientoResponse> getMantenimientosByPriceRange(
            @RequestParam Double minPrecio,
            @RequestParam Double maxPrecio) {
        return mantenimientoUseCase.findByPriceRange(minPrecio, maxPrecio)
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Actualizar mantenimiento
     */
    @PutMapping("/{id}")
    public Mono<MantenimientoResponse> updateMantenimiento(
            @PathVariable String id,
            @Valid @RequestBody MantenimientoRequest request) {
        
        String fechaStr = dateFormat.format(request.getFecha()); // ✅ Conversión
        
        return mantenimientoUseCase.updateMantenimiento(
                id,
                fechaStr,
                request.getDescripcion(),
                request.getPrecio(),
                request.getTipo()
            )
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    @PostMapping("/{id}/repuestos")
    public Mono<MantenimientoResponse> addRepuestoToMantenimiento(
            @PathVariable String id,
            @Valid @RequestBody AddRepuestoRequest request) {
        return mantenimientoUseCase.addRepuestoToMantenimiento(id, request.getRepuestoId())
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    @DeleteMapping("/{id}/repuestos/{repuestoId}")
    public Mono<MantenimientoResponse> removeRepuestoFromMantenimiento(
            @PathVariable String id,
            @PathVariable String repuestoId) {
        return mantenimientoUseCase.removeRepuestoFromMantenimiento(id, repuestoId)
            .flatMap(MantenimientoDtoMapper::toResponseReactive);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMantenimiento(@PathVariable String id) {
        return mantenimientoUseCase.deleteById(id);
    }
}
