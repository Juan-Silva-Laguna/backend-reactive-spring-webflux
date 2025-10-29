package com.company.maintenance.app.domain.exception;

public class MaquinaException extends RuntimeException {

	private MaquinaException(String message) {
		super(message);
	}

	public static MaquinaException mantenimientoNotFound(Double precio) {
		return new MaquinaException("El mantenimientos no es válido: " + precio);
	}

	public static MaquinaException invalidMaquina(String mensaje) {
		return new MaquinaException(mensaje);
	}

	public static MaquinaException notFound(String id) {
		return new MaquinaException("Mquina no encontrada con ID: " + id);
	}
}