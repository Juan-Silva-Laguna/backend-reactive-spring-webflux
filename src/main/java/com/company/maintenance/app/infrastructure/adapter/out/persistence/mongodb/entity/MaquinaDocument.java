package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import java.util.List;

//@Document(collection = "maquinas")
@DynamoDbBean
public class MaquinaDocument {

//	@Id
	private String id;

	private String nombre;

	private String modelo;

	private List<MantenimientoDocument> mantenimientos;

	public MaquinaDocument() {
	}

	public MaquinaDocument(String id, String nombre, String modelo, List<MantenimientoDocument> mantenimientos) {
		this.id = id;
		this.nombre = nombre;
		this.modelo = modelo;
		this.mantenimientos = mantenimientos;
	}

	// Getters y Setters
	@DynamoDbPartitionKey
	@DynamoDbAttribute("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@DynamoDbAttribute("nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@DynamoDbAttribute("modelo")
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
//	@DynamoDbConvertedBy(MantenimientoListConverter.class)
	@DynamoDbAttribute("mantenimientos")
	public List<MantenimientoDocument> getMantenimientos() {
		return mantenimientos;
	}

	public void setMantenimientos(List<MantenimientoDocument> mantenimientos) {
		this.mantenimientos = mantenimientos;
	}
}
