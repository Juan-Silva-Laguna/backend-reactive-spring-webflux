package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import java.util.List;

public class MantenimientoResponse {
    private String id;
    private String fecha;
    private String descripcion;
    private Double precio_total;
    private Double precio_mantenimiento;
    private Double precio_repuestos;
    private List<RepuestoResponse> repuestos;
    private String tipo;
    private String maquinaId;

    // Constructor completo
    public MantenimientoResponse(String id, String fecha, String descripcion, 
                                Double precio_total,Double precio_mantenimiento,Double precio_repuestos,  List<RepuestoResponse> repuestos, 
                                String tipo, String maquinaId) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.precio_total = precio_total;
        this.precio_mantenimiento = precio_mantenimiento;
        this.precio_repuestos = precio_repuestos;
        this.repuestos = repuestos;
        this.tipo = tipo;
        this.maquinaId = maquinaId;
    }

    // Constructor sin maquinaId (para compatibilidad)
    public MantenimientoResponse(String id, String fecha, String descripcion, 
                                Double precio_total, Double precio_mantenimiento,Double precio_repuestos, List<RepuestoResponse> repuestos, 
                                String tipo) {
        this(id, fecha, descripcion, precio_total, precio_mantenimiento, precio_repuestos,  repuestos, tipo, null);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<RepuestoResponse> getRepuestos() { return repuestos; }
    public void setRepuestos(List<RepuestoResponse> repuestos) { this.repuestos = repuestos; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMaquinaId() { return maquinaId; }
    public void setMaquinaId(String maquinaId) { this.maquinaId = maquinaId; }

	public Double getPrecio_total() {
		return precio_total;
	}

	public void setPrecio_total(Double precio_total) {
		this.precio_total = precio_total;
	}

	public Double getPrecio_mantenimiento() {
		return precio_mantenimiento;
	}

	public void setPrecio_mantenimiento(Double precio_mantenimiento) {
		this.precio_mantenimiento = precio_mantenimiento;
	}

	public Double getPrecio_repuestos() {
		return precio_repuestos;
	}

	public void setPrecio_repuestos(Double precio_repuestos) {
		this.precio_repuestos = precio_repuestos;
	}
    
    
}