package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.maintenance.app.application.port.in.RepuestoUseCase;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.RepuestoResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.mapper.RepuestoDtoMapper;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/repuestos")
@PreAuthorize("hasRole('SUPERVISOR')")
public class RepuestoController {
    
    private final RepuestoUseCase respuestoUsecase;
    
    public RepuestoController(RepuestoUseCase respuestoUsecase) {
        this.respuestoUsecase = respuestoUsecase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RepuestoResponse> createRepuesto(@Valid @RequestBody RepuestoRequest request) {
        return respuestoUsecase.createRepuesto(request.getNombre(), request.getPrecio())
            .map(RepuestoDtoMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<RepuestoResponse> getRepuestoById(@PathVariable("id") String id) {
        return respuestoUsecase.findById(id)
            .map(RepuestoDtoMapper::toResponse);
    }

    // READ - Por nombre
    @GetMapping("/nombre/{nombre}")
    public Mono<RepuestoResponse> getRepuestoByNombre(@PathVariable("nombre") String nombre) {
        return respuestoUsecase.findByNombre(nombre)
            .map(RepuestoDtoMapper::toResponse);
    }

    // READ - Listar todos
    @GetMapping
    public Flux<RepuestoResponse> getAllRepuestos() {
        return respuestoUsecase.findAll()
            .map(RepuestoDtoMapper::toResponse);
    }

    // READ - Por rango de precio
    @GetMapping("/precio")
    public Flux<RepuestoResponse> getRepuestosByPriceRange(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max) {
        return respuestoUsecase.findByPriceRange(min, max)
            .map(RepuestoDtoMapper::toResponse);
    }

    // UPDATE - Completo
    @PutMapping("/{id}")
    public Mono<RepuestoResponse> updateRepuesto(
    		@PathVariable("id") String id,
            @Valid @RequestBody RepuestoRequest request) {
        return respuestoUsecase.updateRepuesto(id, request.getNombre(), request.getPrecio())
            .map(RepuestoDtoMapper::toResponse);
    }

    // UPDATE - Solo precio
    @PatchMapping("/{id}/precio")
    public Mono<RepuestoResponse> updatePrice(
            @PathVariable("id") String id,
            @RequestParam("precio") Double precio) {
        return respuestoUsecase.updatePrice(id, precio)
            .map(RepuestoDtoMapper::toResponse);
    }


    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRepuesto(@PathVariable("id") String id) {
        return respuestoUsecase.deleteById(id);
    }
    
}
