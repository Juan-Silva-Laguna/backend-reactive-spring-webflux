package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

//@Document(collection = "repuestos")
@DynamoDbBean
public class RepuestoDocument {

//    @Id
	private String id;

//    @Indexed(unique = true)
	private String nombre;

	private double precio;

	public RepuestoDocument() {
	}

	public RepuestoDocument(String id, String nombre, double precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
	}

	@DynamoDbPartitionKey
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
}
