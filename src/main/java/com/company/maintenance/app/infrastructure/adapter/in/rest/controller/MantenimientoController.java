package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.company.maintenance.app.application.port.in.MantenimientoUseCase;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.AddRepuestoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MantenimientoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.mapper.MantenimientoDtoMapper;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {
    
    private final MantenimientoUseCase mantenimientoUseCase;

    public MantenimientoController(MantenimientoUseCase mantenimientoUseCase) {
        this.mantenimientoUseCase = mantenimientoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TECNICO')")
    public Mono<MantenimientoResponse> createMantenimiento(
            @Valid @RequestBody MantenimientoRequest request) {
        return mantenimientoUseCase.createMantenimiento(
                request.getFecha(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getRepuestosIds(),
                request.getTipo()
            )
            .map(MantenimientoDtoMapper::toResponse);
    }
    
    @PostMapping("/asignar")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('TECNICO')")
    public Mono<MantenimientoResponse> createMantenimientoAsignaMaquina(
            @Valid @RequestBody MantenimientoRequest request) {
        return mantenimientoUseCase.createMantenimientoAsignaMaquina(
                request.getFecha(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getRepuestosIds(),
                request.getTipo(),
                request.getMaquinaId()
            )
            .map(MantenimientoDtoMapper::toResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'TECNICO')")
    public Mono<MantenimientoResponse> getMantenimientoById(@PathVariable("id") String id) {
        return mantenimientoUseCase.findById(id)
            .map(MantenimientoDtoMapper::toResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'TECNICO')")
    public Flux<MantenimientoResponse> getAllMantenimientos() {
        return mantenimientoUseCase.findAll()
            .map(MantenimientoDtoMapper::toResponse);
    }

    @GetMapping("/fecha")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'TECNICO')")
    public Flux<MantenimientoResponse> getMantenimientosByFechaRange(
            @RequestParam("min") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("max") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return mantenimientoUseCase.findByFechaRange(startDate, endDate)
            .map(MantenimientoDtoMapper::toResponse);
    }

    @GetMapping("/precio")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'TECNICO')")
    public Flux<MantenimientoResponse> getMantenimientosByPriceRange(
    		@RequestParam("min") Double min,
    		@RequestParam("max") Double max) {
        return mantenimientoUseCase.findByPriceRange(min, max)
            .map(MantenimientoDtoMapper::toResponse);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public Mono<MantenimientoResponse> updateMantenimiento(
            @PathVariable("id") String id,
            @Valid @RequestBody MantenimientoRequest request) {
        return mantenimientoUseCase.updateMantenimiento(
                id,
                request.getFecha(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getTipo()
            )
            .map(MantenimientoDtoMapper::toResponse);
    }

    @PostMapping("/{id}/repuestos")
    @PreAuthorize("hasRole('TECNICO')")
    public Mono<MantenimientoResponse> addRepuestoToMantenimiento(
            @PathVariable("id") String id,
            @Valid @RequestBody AddRepuestoRequest request) {
        return mantenimientoUseCase.addRepuestoToMantenimiento(id, request.getRepuestoId())
            .map(MantenimientoDtoMapper::toResponse);
    }

    @DeleteMapping("/{id}/repuestos/{repuestoId}")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public Mono<MantenimientoResponse> removeRepuestoFromMantenimiento(
            @PathVariable("id") String id,
            @PathVariable("repuestoId") String repuestoId) {
        return mantenimientoUseCase.removeRepuestoFromMantenimiento(id, repuestoId)
            .map(MantenimientoDtoMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMantenimiento(@PathVariable("id") String id) {
        return mantenimientoUseCase.deleteById(id);
    }
}

