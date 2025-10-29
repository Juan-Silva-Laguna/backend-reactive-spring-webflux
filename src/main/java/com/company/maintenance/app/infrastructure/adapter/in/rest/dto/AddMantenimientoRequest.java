package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class AddMantenimientoRequest {

	@NotBlank(message = "El ID del mantenimiento es obligatorio")
	private String mantenimientoId;

	public String getMantenimientoId() {
		return mantenimientoId;
	}

	public void setMantenimientoId(String mantenimientoId) {
		this.mantenimientoId = mantenimientoId;
	}
}