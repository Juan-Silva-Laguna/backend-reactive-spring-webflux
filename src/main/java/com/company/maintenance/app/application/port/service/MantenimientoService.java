package com.company.maintenance.app.application.port.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.company.maintenance.app.application.port.in.MantenimientoUseCase;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.application.port.out.RepuestoReadRepository;
import com.company.maintenance.app.domain.exception.MantenimientoException;
import com.company.maintenance.app.domain.exception.MaquinaException;
import com.company.maintenance.app.domain.exception.RepuestoException;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.model.Maquina;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MantenimientoService implements MantenimientoUseCase {

    private final MantenimientoRepository mantenimientoRepository;
    private final MaquinaRepository maquinaRepository;
    private final RepuestoReadRepository repuestoRepository;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            MaquinaRepository maquinaRepository,
            RepuestoReadRepository repuestoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.maquinaRepository = maquinaRepository;
        this.repuestoRepository = repuestoRepository;
    }

    @Override
    public Mono<Mantenimiento> createMantenimiento(
            String fecha, String descripcion, Double precio, 
            List<String> repuestosIds, String tipo) {
        
        if (repuestosIds == null || repuestosIds.isEmpty()) {
            Mantenimiento mantenimiento = new Mantenimiento(fecha, descripcion, precio, null, tipo);
            return mantenimientoRepository.save(mantenimiento);
        }

        return Flux.fromIterable(repuestosIds)
            .flatMap(id -> repuestoRepository.findById(id)
                .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
            )
            .collectList()
            .flatMap(repuestos -> { // ✅ Tipo explícito
                Mantenimiento mantenimiento = new Mantenimiento(fecha, descripcion, precio, repuestos, tipo).recalcularPrecioTotal();
                return mantenimientoRepository.save(mantenimiento);
            });
    }

    @Override
    public Mono<Mantenimiento> createMantenimientoAsignaMaquina(
            String maquinaId, String fecha, String descripcion, 
            Double precio, List<String> repuestosIds, String tipo) {
        
        return maquinaRepository.findById(maquinaId)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(maquinaId)))
            .flatMap(maquina -> {
                if (repuestosIds == null || repuestosIds.isEmpty()) {
                    Mantenimiento mantenimiento = new Mantenimiento(
                        fecha, descripcion, precio, null, tipo
                    ).withMaquinaId(maquinaId);
                    
                    return mantenimientoRepository.save(mantenimiento)
                        .flatMap(mantGuardado -> 
                            maquinaRepository.save(maquina.withMantenimiento(mantGuardado))
                                .thenReturn(mantGuardado)
                        );
                }

                return Flux.fromIterable(repuestosIds)
                    .flatMap(id -> repuestoRepository.findById(id)
                        .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
                    )
                    .collectList()
                    .flatMap(repuestos -> { // ✅ Tipo explícito
                        Mantenimiento mantenimiento = new Mantenimiento(
                            fecha, descripcion, precio, repuestos, tipo
                        ).withMaquinaId(maquinaId).recalcularPrecioTotal();
                        
                        return mantenimientoRepository.save(mantenimiento)
                            .flatMap(mantGuardado -> 
                                maquinaRepository.save(maquina.withMantenimiento(mantGuardado))
                                    .thenReturn(mantGuardado)
                            );
                    });
            });
    }

    @Override
    public Mono<Mantenimiento> findById(String id) {
        return mantenimientoRepository.findById(id)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(id)));
    }

    @Override
    public Flux<Mantenimiento> findAll() {
        return mantenimientoRepository.findAll();
    }

    @Override
    public Flux<Mantenimiento> findByFecha(String fecha) {
        return mantenimientoRepository.findByFecha(fecha);
    }

    @Override
    public Flux<Mantenimiento> findByFechaRange(String startDate, String endDate) {
        return mantenimientoRepository.findByFechaBetween(startDate, endDate);
    }

    @Override
    public Flux<Mantenimiento> findByTipo(String tipo) {
        return mantenimientoRepository.findByTipo(tipo);
    }

    @Override
    public Flux<Mantenimiento> findByPriceRange(Double minPrecio, Double maxPrecio) {
        return mantenimientoRepository.findByPrecioBetween(minPrecio, maxPrecio);
    }

    @Override
    public Mono<Mantenimiento> updateMantenimiento(
            String id, String fecha, String descripcion, 
            Double precio, String tipo) {
        
        return mantenimientoRepository.findById(id)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(id)))
            .map(mantenimiento -> mantenimiento
                .withFecha(fecha)
                .withDescripcion(descripcion)
                .withPrecio(precio)
                .withTipo(tipo)
                .recalcularPrecioTotal()
            )
            .flatMap(mantenimiento -> {
                return mantenimientoRepository.save(mantenimiento)
                    .flatMap(mantGuardado -> {
                        if (mantGuardado.getMaquinaId() != null) {
                            return maquinaRepository.findById(mantGuardado.getMaquinaId())
                                .flatMap(maquina -> {
                                    Maquina maquinaActualizada = maquina
                                        .withoutMantenimiento(mantGuardado.getId())
                                        .withMantenimiento(mantGuardado);
                                    return maquinaRepository.save(maquinaActualizada);
                                })
                                .thenReturn(mantGuardado);
                        }
                        return Mono.just(mantGuardado);
                    });
            });
    }

    @Override
    public Mono<Mantenimiento> addRepuestoToMantenimiento(String mantenimientoId, String repuestoId) {
        Mono<Mantenimiento> mantenimientoMono = mantenimientoRepository.findById(mantenimientoId)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId)));

        return Mono.zip(
            mantenimientoMono,
            repuestoRepository.findById(repuestoId)
                .switchIfEmpty(Mono.error(RepuestoException.notFound(repuestoId)))
        )
        .map(tuple -> tuple.getT1()
            .withRepuesto(tuple.getT2()).aumentarPrecio(tuple.getT2().getPrecio())
//            .recalcularPrecioTotal()
        )
        .flatMap(mantenimiento -> {
            return mantenimientoRepository.save(mantenimiento)
                .flatMap(mantGuardado -> {
                    if (mantGuardado.getMaquinaId() != null) {
                        return maquinaRepository.findById(mantGuardado.getMaquinaId())
                            .flatMap(maquina -> {
                                Maquina maquinaActualizada = maquina
                                    .withoutMantenimiento(mantGuardado.getId())
                                    .withMantenimiento(mantGuardado);
                                return maquinaRepository.save(maquinaActualizada);
                            })
                            .thenReturn(mantGuardado);
                    }
                    return Mono.just(mantGuardado);
                });
        });
    }

    @Override
    public Mono<Mantenimiento> removeRepuestoFromMantenimiento(
            String mantenimientoId, String repuestoId) {
        
        return mantenimientoRepository.findById(mantenimientoId)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId)))
            .map(mantenimiento -> mantenimiento
                .withoutRepuesto(repuestoId)
//                .recalcularPrecioTotal()
            )
            .flatMap(mantenimiento -> {
                return mantenimientoRepository.save(mantenimiento)
                    .flatMap(mantGuardado -> {
                        if (mantGuardado.getMaquinaId() != null) {
                            return maquinaRepository.findById(mantGuardado.getMaquinaId())
                                .flatMap(maquina -> {
                                    Maquina maquinaActualizada = maquina
                                        .withoutMantenimiento(mantGuardado.getId())
                                        .withMantenimiento(mantGuardado);
                                    return maquinaRepository.save(maquinaActualizada);
                                })
                                .thenReturn(mantGuardado);
                        }
                        return Mono.just(mantGuardado);
                    });
            });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mantenimientoRepository.existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(MantenimientoException.notFound(id));
                }
                return mantenimientoRepository.deleteById(id);
            });
    }
}
