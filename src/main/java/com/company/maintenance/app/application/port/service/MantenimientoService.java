package com.company.maintenance.app.application.port.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.company.maintenance.app.MaintenanceApplication;
import com.company.maintenance.app.application.port.in.MantenimientoUseCase;
import com.company.maintenance.app.application.port.out.MantenimientoRepository;
import com.company.maintenance.app.application.port.out.MaquinaRepository;
import com.company.maintenance.app.application.port.out.RepuestoRepository;
import com.company.maintenance.app.domain.exception.MantenimientoException;
import com.company.maintenance.app.domain.exception.MaquinaException;
import com.company.maintenance.app.domain.exception.RepuestoException;
import com.company.maintenance.app.domain.model.Mantenimiento;
import com.company.maintenance.app.domain.model.Repuesto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MantenimientoService implements MantenimientoUseCase{

	private final MantenimientoRepository mantenimientoRepository;
    private final RepuestoRepository repuestoRepository;
	private static final Logger log = LoggerFactory.getLogger(MaintenanceApplication.class);

    @Autowired
    MaquinaRepository maquinaRepository;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository,
                               RepuestoRepository repuestoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.repuestoRepository = repuestoRepository;
    }
    
    @Override
    public Mono<Mantenimiento> createMantenimiento(Date fecha, String descripcion, Double precio, List<String> repuestosIds, String tipo) {
        if (repuestosIds == null || repuestosIds.isEmpty()) {
            Mantenimiento mantenimiento = new Mantenimiento(fecha, descripcion, precio, List.of(), tipo, null);
            mantenimiento.recalcularPrecioTotal();
            return mantenimientoRepository.save(mantenimiento);
        }

        return Flux.fromIterable(repuestosIds)
            .flatMap(id -> repuestoRepository.findById(id)
                .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
            )
            .collectList()
            .map(repuestos -> {
                Mantenimiento mantenimiento = new Mantenimiento(fecha, descripcion, precio, repuestos, tipo, null);
                mantenimiento.recalcularPrecioTotal();
                return mantenimiento;
            })
            .flatMap(mantenimientoRepository::save);
    }
    
    @Override
    public Mono<Mantenimiento> createMantenimientoAsignaMaquina(Date fecha, String descripcion, 
                                                                 Double precio, List<String> repuestosIds, 
                                                                 String tipo, String idMaquina) {
        return maquinaRepository.findById(idMaquina)
            .switchIfEmpty(Mono.error(MaquinaException.notFound(idMaquina)))
            .flatMap(maquina -> {
                Mono<List<Repuesto>> repuestosMono = (repuestosIds != null && !repuestosIds.isEmpty())
                    ? Flux.fromIterable(repuestosIds)
                        .flatMap(id -> repuestoRepository.findById(id)
                            .switchIfEmpty(Mono.error(RepuestoException.notFound(id)))
                        )
                        .collectList()
                    : Mono.just(List.of());

                return repuestosMono.flatMap(repuestos -> {
                    Mantenimiento mantenimiento = new Mantenimiento(fecha, descripcion, precio, repuestos, tipo, idMaquina);
                    mantenimiento.recalcularPrecioTotal();
                    return mantenimientoRepository.save(mantenimiento)
                        .flatMap(mGuardado -> {
                            maquina.addMantenimiento(mGuardado);
                            return maquinaRepository.save(maquina)
                                .thenReturn(mGuardado);
                        });
                });
            });
    }


    // READ
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
    public Flux<Mantenimiento> findByTipo(String tipo) {
        if (tipo.toLowerCase() == "preventivo" || tipo.toLowerCase() == "correctivo") {
            return Flux.error(MantenimientoException.invalidMantenimiento("El tipo de mantenimiento no se reconoce"));
        }
        
        return mantenimientoRepository.findByTipo(tipo);
    }
    
    @Override
    public Flux<Mantenimiento> findByFechaRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return Flux.error(MantenimientoException.invalidMantenimiento("Las fechas no pueden ser nulas"));
        }
        if (startDate.after(endDate)) {
            return Flux.error(MantenimientoException.invalidMantenimiento("La fecha inicial debe ser anterior a la fecha final"));
        }
        return mantenimientoRepository.findByFechaBetween(startDate, endDate);
    }

    @Override
    public Flux<Mantenimiento> findByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return Flux.error( MantenimientoException.invalidMantenimiento(
                "Rango de precios inválido: min=" + minPrice + ", max=" + maxPrice
            ));
        }
        return mantenimientoRepository.findByPrecioBetween(minPrice, maxPrice);
    }
    
    @Override
    public Mono<Mantenimiento> updateMantenimiento(String id, Date fecha, String descripcion, Double precio, String tipo) {
        return mantenimientoRepository.findById(id)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(id)))
            .map(mantenimiento -> {
                mantenimiento.setFecha(fecha);
                mantenimiento.setDescripcion(descripcion);
                mantenimiento.setPrecio(precio);
                mantenimiento.recalcularPrecioTotal();
                mantenimiento.setTipo(tipo);
                return mantenimiento;
            })
            .flatMap(mantenimientoRepository::save)
            .flatMap(mGuardado -> {
                if (mGuardado.getMaquinaId() != null && !mGuardado.getMaquinaId().isEmpty()) {
                    return maquinaRepository.findById(mGuardado.getMaquinaId())
                        .flatMap(maquina -> {

                        	List<Mantenimiento> actualizados = maquina.getMantenimientos()
                                    .stream()
                                    .map(m -> m.getId().equals(mGuardado.getId()) ? mGuardado : m)
                                    .toList();

                            maquina.setMantenimientos(actualizados);
                            
                            return maquinaRepository.save(maquina)
                                .thenReturn(mGuardado);
                        })
                        .switchIfEmpty(Mono.just(mGuardado));
                } else {
                    return Mono.just(mGuardado);
                }
            });
    }


//    @Override
//    public Mono<Mantenimiento> addRepuestoToMantenimiento(String mantenimientoId, String repuestoId) {
//        return Mono.zip(
//            mantenimientoRepository.findById(mantenimientoId)
//                .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId))),
//            repuestoRepository.findById(repuestoId)
//                .switchIfEmpty(Mono.error(RepuestoException.notFound(repuestoId)))
//        )
//        .map(tuple -> {
//            Mantenimiento mantenimiento = tuple.getT1();
//            Repuesto repuesto = tuple.getT2();
//            mantenimiento.addRepuesto(repuesto);
//            return mantenimiento;
//        })
//        .flatMap(mantenimientoRepository::save);
//    }
    
    @Override
    public Mono<Mantenimiento> addRepuestoToMantenimiento(String mantenimientoId, String repuestoId) {
        return Mono.zip(
                mantenimientoRepository.findById(mantenimientoId)
                        .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId))),
                repuestoRepository.findById(repuestoId)
                        .switchIfEmpty(Mono.error(RepuestoException.notFound(repuestoId)))
        )
        .flatMap(tuple -> {
            Mantenimiento mantenimiento = tuple.getT1();
            Repuesto repuesto = tuple.getT2();
            mantenimiento.addRepuesto(repuesto);
            mantenimiento.setPrecio(mantenimiento.getPrecio()+repuesto.getPrecio());
            
            return mantenimientoRepository.save(mantenimiento)
                .flatMap(mGuardado -> {
                    if (mantenimiento.getMaquinaId() != null && !mantenimiento.getMaquinaId().isEmpty()) {
                        return maquinaRepository.findById(mantenimiento.getMaquinaId())
                            .flatMap(maquina -> {
                                maquina.addRepuestoMantenimiento(repuesto);
                                return maquinaRepository.save(maquina).thenReturn(mGuardado);
                            })
                            .switchIfEmpty(Mono.just(mGuardado)); // Si no encuentra máquina, devolver mantenimiento
                    }
                    return Mono.just(mGuardado);
                });
        });
    }


    @Override
    public Mono<Mantenimiento> removeRepuestoFromMantenimiento(String mantenimientoId, String repuestoId) {
        return mantenimientoRepository.findById(mantenimientoId)
            .switchIfEmpty(Mono.error(MantenimientoException.notFound(mantenimientoId)))
            .flatMap(mantenimiento -> {
                mantenimiento.removeRepuesto(repuestoId);
                
                return mantenimientoRepository.save(mantenimiento)
                    .flatMap(mGuardado -> {
                        // Solo actualizar máquina si tiene maquinaId asignado
                        if (mantenimiento.getMaquinaId() != null && !mantenimiento.getMaquinaId().isEmpty()) {
                            return maquinaRepository.findById(mantenimiento.getMaquinaId())
                                .flatMap(maquina -> {
                                    maquina.removeRepuestoMantenimiento(repuestoId);
                                    return maquinaRepository.save(maquina).thenReturn(mGuardado);
                                })
                                .switchIfEmpty(Mono.just(mGuardado)); // Si no encuentra máquina, devolver mantenimiento
                        }
                        return Mono.just(mGuardado);
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