package com.company.maintenance.app.domain.exception;

public class MantenimientoException extends RuntimeException {

    private MantenimientoException(String message) {
        super(message);
    }

    public static MantenimientoException repuestoNotFound(Double precio) {
        return new MantenimientoException("El precio no es válido: " + precio);
    }
    
    public static MantenimientoException invalidMantenimiento(String mensaje) {
        return new MantenimientoException(mensaje);
    }
    
    public static MantenimientoException notFound(String id) {
        return new MantenimientoException("Mantenimiento no encontrado con ID: " + id);
    }
}