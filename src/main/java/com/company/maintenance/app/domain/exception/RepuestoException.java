package com.company.maintenance.app.domain.exception;

public class RepuestoException extends RuntimeException {

    private RepuestoException(String message) {
        super(message);
    }
    public static RepuestoException alreadyExists(String nombre) {
        return new RepuestoException("Ya existe un repuesto con el nombre: " + nombre);
    }

    public static RepuestoException invalidPrice(Double precio) {
        return new RepuestoException("El precio no es válido: " + precio);
    }
    
    public static RepuestoException invalidPriceRange(String mensaje) {
        return new RepuestoException(mensaje);
    }
    
    public static RepuestoException notFound(String id) {
        return new RepuestoException("Repuesto no encontrado con ID: " + id);
    }
}