package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

//@Document(collection = "mantenimientos")
@DynamoDbBean
public class MantenimientoDocument {

//	@Id
	private String id;
	private String fecha;
	private String descripcion;
	private Double precio;
	private String tipo;
	private String maquinaId;
	private List<RepuestoDocument> repuestos;

	public MantenimientoDocument() {
	}

	public MantenimientoDocument(String id, String fecha, String descripcion, Double precio,
			List<RepuestoDocument> repuestos, String tipo, String maquinaId) {
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.precio = precio;
		this.repuestos = repuestos;
		this.tipo = tipo;
		this.maquinaId = maquinaId;
	}

	@DynamoDbPartitionKey
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public List<RepuestoDocument> getRepuestos() {
		return repuestos;
	}

	public void setRepuestos(List<RepuestoDocument> repuestos) {
		this.repuestos = repuestos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMaquinaId() {
		return maquinaId;
	}

	public void setMaquinaId(String maquinaId) {
		this.maquinaId = maquinaId;
	}

	
}
