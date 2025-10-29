package com.company.maintenance.app.domain.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Mantenimiento {
	private String id;
	private Date fecha;
	private String descripcion;
	private Double precio;
	private List<Repuesto> repuestos;
	private String tipo;
	private String maquinaId;

	public Mantenimiento(String id, Date fecha, String descripcion, Double precio, List<Repuesto> repuestos,String tipo, String maquinaId) {
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.precio = precio;
		this.tipo= tipo;
		this.maquinaId= maquinaId;
		this.repuestos = repuestos != null ? new ArrayList<>(repuestos) : new ArrayList<>();
		validate();
	}

	public Mantenimiento(Date fecha, String descripcion, Double precio, List<Repuesto> repuestos ,String tipo, String maquinaId) {
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.precio = precio;
		this.tipo= tipo;
		this.maquinaId= maquinaId;
		this.repuestos = repuestos != null ? new ArrayList<>(repuestos) : new ArrayList<>();
		validate();
	}

	private void validate() {
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha del mantenimiento no puede ser nula");
		}
		if (descripcion == null || descripcion.trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción no puede estar vacía");
		}
		if (precio == null || precio < 0) {
			throw new IllegalArgumentException("El precio debe ser mayor o igual a cero");
		}
	}

	public void addRepuesto(Repuesto repuesto) {
		if (repuesto == null) {
			throw new IllegalArgumentException("El repuesto no puede ser nulo");
		}
		this.repuestos.add(repuesto);
//		recalcularPrecioTotal();
//		this.precio += repuesto.getPrecio();
	}
	
//	public void removeRepuesto(String repuestoId) {
//		this.repuestos.removeIf(r -> {
//			if  (r.getId().equals(repuestoId)){
//				this.precio -= r.getPrecio();
//				return true;
//			}
//			return false;
//		});
//	}
	
	public void removeRepuesto(String repuestoId) {
	    this.repuestos.stream()
	        .filter(r -> r.getId().equals(repuestoId))
	        .findFirst()
	        .ifPresent(r -> {
	            this.repuestos.remove(r);
	            this.precio -= r.getPrecio();
	        });
	}
	
	public void recalcularPrecioTotal() {
		double total = this.precio;
		for (Repuesto repuesto : repuestos) {
			total += repuesto.getPrecio();
		}
		this.precio = total;
	}

	public int getCantidadRepuestos() {
		return repuestos != null ? repuestos.size() : 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		validate();
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
		validate();
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
		validate();
	}

	public List<Repuesto> getRepuestos() {
		return repuestos != null ? new ArrayList<>(repuestos) : new ArrayList<>();
	}

	public void setRepuestos(List<Repuesto> repuestos) {
		this.repuestos = repuestos != null ? new ArrayList<>(repuestos) : new ArrayList<>();
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
