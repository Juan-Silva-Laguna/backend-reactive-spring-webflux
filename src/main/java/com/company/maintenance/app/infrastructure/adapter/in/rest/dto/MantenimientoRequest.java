package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MantenimientoRequest {

	@NotNull(message = "La fecha es obligatoria")
	@PastOrPresent(message = "La fecha no puede ser futura")
	private Date fecha;

	@NotBlank(message = "La descripción es obligatoria")
	@Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser mayor o igual a 0")
	private Double precio;

	@NotBlank(message = "El tipo de mantenimiento es obligatorio")
	@Pattern(regexp = "^(preventivo|correctivo)$", message = "El tipo debe ser 'preventivo' o 'correctivo'")
	private String tipo;
	
	private String maquinaId;

	private List<String> repuestosIds;

	// Getters y Setters
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public List<String> getRepuestosIds() {
		return repuestosIds;
	}

	public void setRepuestosIds(List<String> repuestosIds) {
		this.repuestosIds = repuestosIds;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMaquinaId() {
		return maquinaId;
	}

	public void setMaquinaId(String maquinaId) {
		this.maquinaId = maquinaId;
	}
	
	

}
