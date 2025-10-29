package com.company.maintenance.app.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class AddRepuestoRequest {
    
    @NotBlank(message = "El ID del repuesto es obligatorio")
    private String repuestoId;

    public String getRepuestoId() { return repuestoId; }
    public void setRepuestoId(String repuestoId) { this.repuestoId = repuestoId; }
}