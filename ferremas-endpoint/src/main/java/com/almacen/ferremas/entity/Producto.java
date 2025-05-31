package com.almacen.ferremas.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name="producto")

public class Producto {
    @Id
    private int producto_id;
    private String nombre;
    private String descripcion;
    private float precio;
    private String imagen;
    private int stock;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles_ventas;

    public Producto() {
    }

    public Producto(int producto_id, String nombre, String descripcion, float precio, String imagen, int stock, List<DetalleVenta> detalles_ventas) {
        this.producto_id = producto_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
        this.detalles_ventas = detalles_ventas;
    }

    public int getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
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

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<DetalleVenta> getDetalles_ventas() {
        return detalles_ventas;
    }

    public void setDetalles_ventas(List<DetalleVenta> detalles_ventas) {
        this.detalles_ventas = detalles_ventas;
    }
}
