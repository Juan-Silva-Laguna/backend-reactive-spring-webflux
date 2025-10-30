package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.company.maintenance.app.application.port.in.RepuestoUseCase;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.mapper.RepuestoDtoMapper;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/repuestos")
@PreAuthorize("hasAnyRole('TECNICO', 'SUPERVISOR')")
public class RepuestoController {
    
    private final RepuestoUseCase repuestoUseCase;

    public RepuestoController(RepuestoUseCase repuestoUseCase) {
        this.repuestoUseCase = repuestoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RepuestoResponse> createRepuesto(@Valid @RequestBody RepuestoRequest request) {
        return repuestoUseCase.createRepuesto(request.getNombre(), request.getPrecio())
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    @GetMapping("/{id}")
    public Mono<RepuestoResponse> getRepuestoById(@PathVariable String id) {
        return repuestoUseCase.findById(id)
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    @GetMapping("/nombre/{nombre}")
    public Mono<RepuestoResponse> getRepuestoByNombre(@PathVariable String nombre) {
        return repuestoUseCase.findByNombre(nombre)
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    @GetMapping
    public Flux<RepuestoResponse> getAllRepuestos() {
        return repuestoUseCase.findAll()
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Búsqueda por rango de precios
     */
    @GetMapping("/precio-range")
    public Flux<RepuestoResponse> getRepuestosByPriceRange(
            @RequestParam Double minPrecio,
            @RequestParam Double maxPrecio) {
        return repuestoUseCase.findByPriceRange(minPrecio, maxPrecio)
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    @PutMapping("/{id}")
    public Mono<RepuestoResponse> updateRepuesto(
            @PathVariable String id,
            @Valid @RequestBody RepuestoRequest request) {
        return repuestoUseCase.updateRepuesto(id, request.getNombre(), request.getPrecio())
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Actualizar solo el precio
     */
    @PatchMapping("/{id}/precio")
    public Mono<RepuestoResponse> updatePrecio(
            @PathVariable String id,
            @RequestParam Double precio) {
        return repuestoUseCase.updatePrice(id, precio)
            .flatMap(RepuestoDtoMapper::toResponseReactive);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRepuesto(@PathVariable String id) {
        return repuestoUseCase.deleteById(id);
    }
}
