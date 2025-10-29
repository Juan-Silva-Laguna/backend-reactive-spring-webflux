package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import java.util.List;

public class MaquinaResponse {
    private String id;
    private String nombre;
    private String modelo;
    private List<MantenimientoResponse> mantenimientos;
    private Integer cantidadMantenimientos;

    public MaquinaResponse(String id, String nombre, String modelo, List<MantenimientoResponse> mantenimientos) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
        this.mantenimientos = mantenimientos;
        this.cantidadMantenimientos = mantenimientos != null ? mantenimientos.size() : 0;
    }

    // Getters

    public Integer getCantidadMantenimientos() { return cantidadMantenimientos; }

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getModelo() {
		return modelo;
	}

	public List<MantenimientoResponse> getMantenimientos() {
		return mantenimientos;
	}
}
