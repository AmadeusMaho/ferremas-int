package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="detalle_venta")

public class DetalleVenta {
    @Id
    private int detalle_venta_id;
    private int cantidad;
    private double precio_unit;

    @ManyToOne
    @JoinColumn(name="venta_id", nullable = false)
    @JsonIgnoreProperties("venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name="producto_id", nullable = false)
    @JsonIgnoreProperties("producto")
    private Producto producto;

    public DetalleVenta() {
    }

    public DetalleVenta(int detalle_venta_id, int cantidad, double precio_unit, Venta venta, Producto producto) {
        this.detalle_venta_id = detalle_venta_id;
        this.cantidad = cantidad;
        this.precio_unit = precio_unit;
        this.venta = venta;
        this.producto = producto;
    }

    public int getDetalle_venta_id() {
        return detalle_venta_id;
    }

    public void setDetalle_venta_id(int detalle_venta_id) {
        this.detalle_venta_id = detalle_venta_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unit() {
        return precio_unit;
    }

    public void setPrecio_unit(double precio_unit) {
        this.precio_unit = precio_unit;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
