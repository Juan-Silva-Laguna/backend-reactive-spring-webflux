package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.company.maintenance.app.application.port.in.MaquinaUseCase;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.AddMantenimientoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MaquinaRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MaquinaResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.mapper.MaquinaDtoMapper;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/maquinas")
@PreAuthorize("hasAnyRole('TECNICO', 'SUPERVISOR')")
public class MaquinaController {
    
    private final MaquinaUseCase maquinaUseCase;

    public MaquinaController(MaquinaUseCase maquinaUseCase) {
        this.maquinaUseCase = maquinaUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MaquinaResponse> createMaquina(
            @Valid @RequestBody MaquinaRequest request) {
        return maquinaUseCase.createMaquina(
                request.getNombre(),
                request.getModelo(),
                request.getMantenimientosIds()
            )
            .map(MaquinaDtoMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<MaquinaResponse> getMaquinaById(@PathVariable("id") String id) {
        return maquinaUseCase.findById(id)
            .map(MaquinaDtoMapper::toResponse);
    }
    
    @GetMapping("/nombre/{nombre}")
    public Mono<MaquinaResponse> getMaquinaByNombre(@PathVariable("nombre") String nombre) {
        return maquinaUseCase.findByNombre(nombre)
            .map(MaquinaDtoMapper::toResponse);
    }

    @GetMapping
    public Flux<MaquinaResponse> getAllMaquinas() {
        return maquinaUseCase.findAll()
            .map(MaquinaDtoMapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<MaquinaResponse> updateMaquina(
            @PathVariable("id") String id,
            @Valid @RequestBody MaquinaRequest request) {
        return maquinaUseCase.updateMaquina(
                id,
                request.getNombre(),
                request.getModelo()
            )
            .map(MaquinaDtoMapper::toResponse);
    }

    @PostMapping("/{id}/mantenimientos")
    public Mono<MaquinaResponse> addMantenimientoToMaquina(
            @PathVariable("id") String id,
            @Valid @RequestBody AddMantenimientoRequest request) {
        return maquinaUseCase.addMantenimientoToMaquina(id, request.getMantenimientoId())
            .map(MaquinaDtoMapper::toResponse);
    }

    @DeleteMapping("/{id}/mantenimientos/{mantenimientoId}")
    public Mono<MaquinaResponse> removeMantenimientoFromMaquina(
            @PathVariable("id") String id,
            @PathVariable("mantenimientoId") String mantenimientoId) {
        return maquinaUseCase.removeMantenimientoFromMaquina(id, mantenimientoId)
            .map(MaquinaDtoMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMaquina(@PathVariable("id") String id) {
        return maquinaUseCase.deleteById(id);
    }
}

