package com.company.maintenance.app.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Maquina {
    private final String id;
    private final String nombre;
    private final String modelo;
    private final List<Mantenimiento> mantenimientos;

    public Maquina(String id, String nombre, String modelo, List<Mantenimiento> mantenimientos) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
        this.mantenimientos = mantenimientos != null 
            ? Collections.unmodifiableList(new ArrayList<>(mantenimientos))
            : Collections.emptyList();
    }

    public Maquina(String nombre, String modelo, List<Mantenimiento> mantenimientos) {
        this(null, nombre, modelo, mantenimientos);
    }

    public Maquina(String nombre, String modelo) {
        this(null, nombre, modelo, Collections.emptyList());
    }

    // ✅ Métodos inmutables
    public Maquina withId(String id) {
        return new Maquina(id, this.nombre, this.modelo, this.mantenimientos);
    }

    public Maquina withNombre(String nombre) {
        return new Maquina(this.id, nombre, this.modelo, this.mantenimientos);
    }

    public Maquina withModelo(String modelo) {
        return new Maquina(this.id, this.nombre, modelo, this.mantenimientos);
    }

    public Maquina withNombreAndModelo(String nombre, String modelo) {
        return new Maquina(this.id, nombre, modelo, this.mantenimientos);
    }

    public Maquina withMantenimiento(Mantenimiento mantenimiento) {
        List<Mantenimiento> nuevaLista = new ArrayList<>(this.mantenimientos);
        nuevaLista.add(mantenimiento);
        return new Maquina(this.id, this.nombre, this.modelo, nuevaLista);
    }

    public Maquina withoutMantenimiento(String mantenimientoId) {
        List<Mantenimiento> nuevaLista = this.mantenimientos.stream()
            .filter(m -> !m.getId().equals(mantenimientoId))
            .toList();
        return new Maquina(this.id, this.nombre, this.modelo, nuevaLista);
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getModelo() { return modelo; }
    public List<Mantenimiento> getMantenimientos() { return mantenimientos; }
    public int getCantidadMantenimientos() { return mantenimientos.size(); }
}