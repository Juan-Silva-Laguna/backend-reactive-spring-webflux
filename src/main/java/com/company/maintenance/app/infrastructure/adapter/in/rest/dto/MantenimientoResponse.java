package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import java.util.Date;
import java.util.List;

public class MantenimientoResponse {
    private String id;
    private Date fecha;
    private String descripcion;
    private Double precio;
    private List<RepuestoResponse> repuestos;
    private Integer cantidadRepuestos;
    private String tipo;


    public MantenimientoResponse(String id, Date fecha, String descripcion, Double precio, List<RepuestoResponse> repuestos, String tipo) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.precio = precio;
        this.repuestos = repuestos;
        this.cantidadRepuestos = repuestos != null ? repuestos.size() : 0;
		this.tipo = tipo;

    }

    // Getters
    public String getId() { return id; }
    public Date getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public List<RepuestoResponse> getRepuestos() { return repuestos; }
    public Integer getCantidadRepuestos() { return cantidadRepuestos; }
    public String getTipo() { return tipo; }

}
