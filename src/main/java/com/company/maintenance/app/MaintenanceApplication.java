package com.company.maintenance.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MaintenanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaintenanceApplication.class, args);
	}

	@PostConstruct
	public void checkDevTools() {
	    System.out.println("DevTools restart enabled? " + Boolean.getBoolean("spring.devtools.restart.enabled"));
	}


}



