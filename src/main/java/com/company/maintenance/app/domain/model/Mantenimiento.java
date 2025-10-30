package com.company.maintenance.app.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class Mantenimiento {
    private final String id;
    private final String fecha;
    private final String descripcion;
    private final Double precio;
    private final List<Repuesto> repuestos;
    private final String tipo;
    private final String maquinaId; // ✅ Agregado para la relación


    // Constructor completo
    public Mantenimiento(String id, String fecha, String descripcion, 
                        Double precio, List<Repuesto> repuestos, String tipo, String maquinaId) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.precio = precio;
        this.repuestos = repuestos != null 
            ? Collections.unmodifiableList(new ArrayList<>(repuestos))
            : Collections.emptyList();
        this.tipo = tipo;
        this.maquinaId = maquinaId;
    }

    // Constructor sin maquinaId (para compatibilidad)
    public Mantenimiento(String id, String fecha, String descripcion, 
                        Double precio, List<Repuesto> repuestos, String tipo) {
        this(id, fecha, descripcion, precio, repuestos, tipo, null);
    }

    // Constructor sin ID
    public Mantenimiento(String fecha, String descripcion, 
                        Double precio, List<Repuesto> repuestos, String tipo) {
        this(null, fecha, descripcion, precio, repuestos, tipo, null);
    }

    // ✅ Métodos inmutables
    public Mantenimiento withId(String id) {
        return new Mantenimiento(id, this.fecha, this.descripcion, 
                                this.precio, this.repuestos, this.tipo, this.maquinaId);
    }

    public Mantenimiento withMaquinaId(String maquinaId) {
        return new Mantenimiento(this.id, this.fecha, this.descripcion, 
                                this.precio, this.repuestos, this.tipo, maquinaId);
    }

    public Mantenimiento withFecha(String fecha) {
        return new Mantenimiento(this.id, fecha, this.descripcion, 
                                this.precio, this.repuestos, this.tipo, this.maquinaId);
    }

    public Mantenimiento withDescripcion(String descripcion) {
        return new Mantenimiento(this.id, this.fecha, descripcion, 
                                this.precio, this.repuestos, this.tipo, this.maquinaId);
    }

    public Mantenimiento withPrecio(Double precio) {
        return new Mantenimiento(this.id, this.fecha, this.descripcion, 
                                precio, this.repuestos, this.tipo, this.maquinaId);
    }

    public Mantenimiento withTipo(String tipo) {
        return new Mantenimiento(this.id, this.fecha, this.descripcion, 
                                this.precio, this.repuestos, tipo, this.maquinaId);
    }

    public Mantenimiento withRepuesto(Repuesto repuesto) {
        List<Repuesto> nuevaLista = new ArrayList<>(this.repuestos);
        nuevaLista.add(repuesto);
        return new Mantenimiento(this.id, this.fecha, this.descripcion, 
                                this.precio, nuevaLista, this.tipo, this.maquinaId);
    }

    public Mantenimiento withoutRepuesto(String repuestoId) {
        Optional<Repuesto> repuestoAEliminar = this.repuestos.stream()
                .filter(r -> r.getId().equals(repuestoId))
                .findFirst();

            double nuevoPrecio = this.precio;
            if (repuestoAEliminar.isPresent()) {
                nuevoPrecio -= repuestoAEliminar.get().getPrecio();
            }
        List<Repuesto> nuevaLista = this.repuestos.stream()
            .filter(r -> !r.getId().equals(repuestoId))
            .toList();
        return new Mantenimiento(this.id, this.fecha, this.descripcion, 
        		nuevoPrecio, nuevaLista, this.tipo, this.maquinaId);
    }

    public Mantenimiento withPrecioIncremented(Double increment) {
        return new Mantenimiento(this.id, this.fecha, this.descripcion, 
                                this.precio + increment, this.repuestos, this.tipo, this.maquinaId);
    }

    public Mantenimiento recalcularPrecioTotal() {
        return withPrecio(getPrecioMantenimiento()+getTotalRepuestos());
    }
    
    public Mantenimiento aumentarPrecio(Double precio) {
    	double total = precio + this.precio;
        return withPrecio(total);
    }
    
    public Mantenimiento disminuirPrecio(Double precio) {
    	double total = this.precio - precio;
        return withPrecio(total);
    }

    public String getId() { return id; }
    public String getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public List<Repuesto> getRepuestos() { return repuestos; }
    public String getTipo() { return tipo; }
    public String getMaquinaId() { return maquinaId; }
    
    public Integer getCantidadRepuestos() {
        return repuestos != null ? repuestos.size() : 0;
    }
    
    public Double getTotalRepuestos() {
        BigDecimal total = repuestos.stream()
            .map(Repuesto::getPrecio)
            .map(BigDecimal::valueOf)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getPrecioMantenimiento() {
        double totalRepuestos = getTotalRepuestos();
        double resultado = this.precio >= totalRepuestos 
            ? this.precio - totalRepuestos 
            : this.precio;
        return Math.round(resultado * 100.0) / 100.0;
    }

}