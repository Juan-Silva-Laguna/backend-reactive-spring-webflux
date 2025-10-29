package com.company.maintenance.app.domain.model;

import java.util.List;

public class Maquina {
	private String id;
	
	private String nombre;
	
	private String modelo;

	private List<Mantenimiento> mantenimientos;

	public Maquina(String id, String nombre, String modelo, List<Mantenimiento> mantenimientos) {
		this.id = id;
		this.nombre = nombre;
		this.modelo = modelo;
		this.mantenimientos = mantenimientos;
	}
	
	public Maquina(String nombre, String modelo, List<Mantenimiento> mantenimientos) {
		this.nombre = nombre;
		this.modelo = modelo;
		this.mantenimientos = mantenimientos;
		validate();
	}
	
	public Maquina(String nombre, String modelo) {
		this.nombre = nombre;
		this.modelo = modelo;
		validate();
	}
	
    private void validate() {
        if (nombre == null) {
            throw new IllegalArgumentException("La nombre del mantenimiento no puede ser nula");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
    }
    
    public void addRepuestoMantenimiento(Repuesto repuesto) {
        if (repuesto == null) {
            throw new IllegalArgumentException("El Repuesto no puede ser nulo");
        }
        this.mantenimientos.forEach(val -> {
        	val.addRepuesto(repuesto);
            val.setPrecio(val.getPrecio()+repuesto.getPrecio());
        });
    }

    public void removeRepuestoMantenimiento(String id) {
        this.mantenimientos.forEach(val -> {
        	val.removeRepuesto(id);
        });
    }

    

    public void addMantenimiento(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            throw new IllegalArgumentException("El Mantenimiento no puede ser nulo");
        }
        this.mantenimientos.add(mantenimiento);
    }
    
    public void removeMantenimiento(String mantenimientoId) {
        this.mantenimientos.removeIf(r -> r.getId().equals(mantenimientoId));
    }

    public int getCantidadMantenimientos() {
        return mantenimientos != null ? mantenimientos.size() : 0;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public List<Mantenimiento> getMantenimientos() {
		return mantenimientos;
	}

	public void setMantenimientos(List<Mantenimiento> mantenimientos) {
		this.mantenimientos = mantenimientos;
	}

	
}
