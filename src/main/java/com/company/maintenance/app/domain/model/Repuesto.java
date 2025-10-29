package com.company.maintenance.app.domain.model;

public class Repuesto {
    private String id;
    private String nombre;
    private Double precio;

    public Repuesto(String id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        validate();
    }

    public Repuesto(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
        validate();
    }

    private void validate() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del repuesto no puede estar vacío");
        }
        if (precio == null || precio < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a cero");
        }
    }

    public void applyDiscount(Double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }
        this.precio = this.precio - (this.precio * percentage / 100);
    }

    public void increasePrice(Double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("El incremento debe ser positivo");
        }
        this.precio = this.precio + (this.precio * percentage / 100);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { 
        this.nombre = nombre;
        validate();
    }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { 
        this.precio = precio;
        validate();
    }
}
