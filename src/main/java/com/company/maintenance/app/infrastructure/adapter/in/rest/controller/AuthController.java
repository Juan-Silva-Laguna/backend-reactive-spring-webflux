package com.company.maintenance.app.infrastructure.adapter.in.rest.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity.RepuestoDocument;
import com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.repository.RepuestoDynamoRepository;
import com.company.maintenance.app.infrastructure.security.JwtService;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    public AuthController(JwtService jwtService, ReactiveUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private RepuestoDynamoRepository repository;

    
    @PostMapping("/login")
    public Mono<String> login(@RequestParam("username") String username, @RequestParam("password")  String password) {
        return userDetailsService.findByUsername(username)
                .filter(user -> user.getPassword().equals("{noop}" + password))
                .map(jwtService::generateToken)
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")));
    }
    
    @GetMapping("/aws")
    public String credencialesAws() {
    	try {
            var credentials = DefaultCredentialsProvider.create().resolveCredentials();
            return "AWS Access Key ID: " + credentials.accessKeyId() + " AWS Secret Access Key: " + (credentials.secretAccessKey().isEmpty() ? "VACÍA" : "*****");
        } catch (Exception e) {
            return "No se encontraron credenciales: " + e.getMessage();
        }
    }
    
    @GetMapping("/test-dynamo")
    public Mono<String> testDynamo() {
        RepuestoDocument repuesto = new RepuestoDocument(UUID.randomUUID().toString(), "Repuesto Test", 123.45);
        return repository.save(repuesto)
                         .flatMap(saved -> repository.findById(saved.getId()))
                         .map(found -> "Guardado y recuperado: " + found.getId() + " - " + found.getNombre() + " - " + found.getPrecio())
                         .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

}