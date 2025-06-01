package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="direccion")

public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direccion_id")
    private int direccionId;
    private String nombre;
    private String descripcion;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="cliente_id", nullable = false)
    //@JsonIgnoreProperties("cliente")
    private Cliente cliente;

    @JsonManagedReference
    @OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL, orphanRemoval = true)
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
