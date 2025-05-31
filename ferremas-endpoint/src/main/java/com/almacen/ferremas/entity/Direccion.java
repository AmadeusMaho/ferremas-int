package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="direccion")

public class Direccion {
    @Id
    private int direccion_id;
    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="cliente_id", nullable = false)
    @JsonIgnoreProperties("cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Despacho> despachos;

    public Direccion() {
    }

    public Direccion(int direccion_id, String nombre, String descripcion, Cliente cliente, List<Despacho> despachos) {
        this.direccion_id = direccion_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cliente = cliente;
        this.despachos = despachos;
    }

    public int getDireccion_id() {
        return direccion_id;
    }

    public void setDireccion_id(int direccion_id) {
        this.direccion_id = direccion_id;
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
