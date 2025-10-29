package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

public class MaquinaRequest {
    
	@NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    
    private List<String> mantenimientosIds;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getMantenimientosIds() {
		return mantenimientosIds;
	}

	public void setMantenimientosIds(List<String> mantenimientosIds) {
		this.mantenimientosIds = mantenimientosIds;
	}

}
