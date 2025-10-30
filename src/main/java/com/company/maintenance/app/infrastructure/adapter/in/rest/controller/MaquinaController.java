package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.company.maintenance.app.application.port.in.MaquinaUseCase;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.AddMantenimientoRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MaquinaRequest;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.MaquinaResponse;
import com.company.maintenance.app.infrastructure.adapter.in.rest.mapper.MaquinaDtoMapper;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/maquinas")
@PreAuthorize("hasAnyRole('TECNICO', 'SUPERVISOR')")
public class MaquinaController {
    
    private final MaquinaUseCase maquinaUseCase;

    public MaquinaController(MaquinaUseCase maquinaUseCase) {
        this.maquinaUseCase = maquinaUseCase;
    }

    /**
     * ✅ Crea una máquina - Flujo 100% reactivo
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MaquinaResponse> createMaquina(@Valid @RequestBody MaquinaRequest request) {
        return maquinaUseCase.createMaquina(
                request.getNombre(),
                request.getModelo(),
                request.getMantenimientosIds()
            )
            .flatMap(MaquinaDtoMapper::toResponseReactive); // ✅ Mapper reactivo
    }

    /**
     * ✅ Obtiene máquina por ID
     */
    @GetMapping("/{id}")
    public Mono<MaquinaResponse> getMaquinaById(@PathVariable String id) {
        return maquinaUseCase.findById(id)
            .flatMap(MaquinaDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Obtiene máquina por nombre
     */
    @GetMapping("/nombre/{nombre}")
    public Mono<MaquinaResponse> getMaquinaByNombre(@PathVariable String nombre) {
        return maquinaUseCase.findByNombre(nombre)
            .flatMap(MaquinaDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Lista todas las máquinas - Stream reactivo
     */
    @GetMapping
    public Flux<MaquinaResponse> getAllMaquinas() {
        return maquinaUseCase.findAll()
            .flatMap(MaquinaDtoMapper::toResponseReactive); // ✅ Cada elemento se procesa reactivamente
    }

    /**
     * ✅ Actualiza una máquina
     */
    @PutMapping("/{id}")
    public Mono<MaquinaResponse> updateMaquina(
            @PathVariable String id,
            @Valid @RequestBody MaquinaRequest request) {
        return maquinaUseCase.updateMaquina(id, request.getNombre(), request.getModelo())
            .flatMap(MaquinaDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Agrega un mantenimiento a una máquina
     */
    @PostMapping("/{id}/mantenimientos")
    public Mono<MaquinaResponse> addMantenimientoToMaquina(
            @PathVariable String id,
            @Valid @RequestBody AddMantenimientoRequest request) {
        return maquinaUseCase.addMantenimientoToMaquina(id, request.getMantenimientoId())
            .flatMap(MaquinaDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Remueve un mantenimiento de una máquina
     */
    @DeleteMapping("/{id}/mantenimientos/{mantenimientoId}")
    public Mono<MaquinaResponse> removeMantenimientoFromMaquina(
            @PathVariable String id,
            @PathVariable String mantenimientoId) {
        return maquinaUseCase.removeMantenimientoFromMaquina(id, mantenimientoId)
            .flatMap(MaquinaDtoMapper::toResponseReactive);
    }

    /**
     * ✅ Elimina una máquina
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMaquina(@PathVariable String id) {
        return maquinaUseCase.deleteById(id);
    }
}
