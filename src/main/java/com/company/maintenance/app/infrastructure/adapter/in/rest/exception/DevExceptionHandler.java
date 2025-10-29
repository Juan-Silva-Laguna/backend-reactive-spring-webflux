package com.company.maintenance.app.infrastructure.adapter.in.rest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice
public class DevExceptionHandler {

//    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handle(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        Map<String,Object> body = new HashMap<>();
        body.put("message", "Error interno del servidor");
        body.put("error", ex.getClass().getSimpleName());
        body.put("status", 500);
        body.put("trace", sw.toString());
        return ResponseEntity.status(500).body(body);
    }
}