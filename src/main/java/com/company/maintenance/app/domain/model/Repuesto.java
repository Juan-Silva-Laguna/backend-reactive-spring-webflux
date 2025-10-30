package com.company.maintenance.app.domain.model;

public final class Repuesto {
    private final String id;
    private final String nombre;
    private final Double precio;

    public Repuesto(String id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Repuesto(String nombre, Double precio) {
        this(null, nombre, precio);
    }

    // ✅ Métodos inmutables
    public Repuesto withId(String id) {
        return new Repuesto(id, this.nombre, this.precio);
    }

    public Repuesto withNombre(String nombre) {
        return new Repuesto(this.id, nombre, this.precio);
    }

    public Repuesto withPrecio(Double precio) {
        return new Repuesto(this.id, this.nombre, precio);
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
}
