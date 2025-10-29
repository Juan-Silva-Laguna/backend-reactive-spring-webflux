package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

public class RepuestoResponse {
    private String id;
    private String nombre;
    private Double precio;

    public RepuestoResponse(String id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
}
