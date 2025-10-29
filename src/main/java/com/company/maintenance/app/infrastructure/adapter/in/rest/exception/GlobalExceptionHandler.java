package com.company.maintenance.app.infrastructure.adapter.in.rest.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.http.HttpStatus;

import com.company.maintenance.app.domain.exception.MantenimientoException;
import com.company.maintenance.app.domain.exception.RepuestoException;
import com.company.maintenance.app.infrastructure.adapter.in.rest.dto.ErrorResponse;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RepuestoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleRepuestoException(RepuestoException ex) {
        String msg = ex.getMessage().toLowerCase().contains("no encontrado") 
            ? "NOT_FOUND" 
            : "BAD_REQUEST";
        int code = msg.equals("NOT_FOUND") ? 404 : 400;

        return Mono.just(new ErrorResponse(ex.getMessage(), msg, code));
    }

    @ExceptionHandler(MantenimientoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleMantenimientoException(MantenimientoException ex) {
        String msg = ex.getMessage().toLowerCase().contains("no encontrado") 
            ? "NOT_FOUND" 
            : "BAD_REQUEST";
        int code = msg.equals("NOT_FOUND") ? 404 : 400;

        return Mono.just(new ErrorResponse(ex.getMessage(), msg, code));
    } 

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidationErrors(WebExchangeBindException ex) {
        String errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce((a, b) -> a + ", " + b)
            .orElse("Error de validación");

        return Mono.just(new ErrorResponse(
            "Errores de validación: " + errors,
            "VALIDATION_ERROR",
            400
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(new ErrorResponse(
            ex.getMessage(),
            "BAD_REQUEST",
            400
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGenericError(Exception ex) {
        return Mono.just(new ErrorResponse(
            "Error interno del servidor",
            "INTERNAL_SERVER_ERROR",
            500
        ));
    }

    private Mono<ErrorResponse> buildError(String message, String code, HttpStatus status) {
        return Mono.just(new ErrorResponse(message, code, status.value()));
    }
}
