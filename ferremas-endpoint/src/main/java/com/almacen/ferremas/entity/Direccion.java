package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direccion_id")
    private int direccionId;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"ventas", "direcciones"}) // Ignoramos ventas y direcciones para no recursión excesiva
    private Cliente cliente;

    @OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("direccion") // Para evitar recursión desde despachos
    private List<Despacho> despachos;

    public Direccion() {
    }

    public Direccion(List<Despacho> despachos, Cliente cliente, String descripcion, String nombre, int direccionId) {
        this.despachos = despachos;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.direccionId = direccionId;
    }

    // Getters y setters

    public int getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(int direccionId) {
        this.direccionId = direccionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Despacho> getDespachos() {
        return despachos;
    }

    public void setDespachos(List<Despacho> despachos) {
        this.despachos = despachos;
    }
}
