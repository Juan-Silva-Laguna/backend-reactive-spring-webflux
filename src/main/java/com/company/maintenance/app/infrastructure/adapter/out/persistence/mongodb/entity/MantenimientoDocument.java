package com.company.maintenance.app.infrastructure.adapter.out.persistence.mongodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class MantenimientoDocument {

    private String id;
    private String fecha;
    private String descripcion;
    private Double precio;
    private List<RepuestoDocument> repuestos;
    private String tipo;
    private String maquinaId;

    public MantenimientoDocument() {}

    public MantenimientoDocument(String id, String fecha, String descripcion,
                                 Double precio, List<RepuestoDocument> repuestos,
                                 String tipo, String maquinaId) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.precio = precio;
        this.repuestos = repuestos;
        this.tipo = tipo;
        this.maquinaId = maquinaId;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @DynamoDbAttribute("fecha")
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    @DynamoDbAttribute("descripcion")
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @DynamoDbAttribute("precio")
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    @DynamoDbAttribute("repuestos")
    public List<RepuestoDocument> getRepuestos() { return repuestos; }
    public void setRepuestos(List<RepuestoDocument> repuestos) { this.repuestos = repuestos; }

    @DynamoDbAttribute("tipo")
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @DynamoDbAttribute("maquinaId") 
    public String getMaquinaId() { return maquinaId; }
    public void setMaquinaId(String maquinaId) { this.maquinaId = maquinaId; }
}
