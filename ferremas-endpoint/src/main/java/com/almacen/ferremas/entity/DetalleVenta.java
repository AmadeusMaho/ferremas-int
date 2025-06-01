package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name="detalle_venta")

public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_venta_id")
    private int detalleVentaId;
    private int cantidad;
    @Column(name = "precio_unit")
    private double precioUnit;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="venta_id", nullable = false)
    //@JsonIgnoreProperties("venta")
    private Venta venta;

    @ManyToOne
    //@JsonBackReference
    @JoinColumn(name="producto_id", nullable = false)
    @JsonIgnoreProperties({"detalles_ventas"})
    private Producto producto;

    public DetalleVenta() {
    }

    public DetalleVenta(int detalleVentaId, int cantidad, double precioUnit, Venta venta, Producto producto) {
        this.detalleVentaId = detalleVentaId;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
        this.venta = venta;
        this.producto = producto;
    }

    public int getDetalleVentaId() {
        return detalleVentaId;
    }

    public void setDetalleVentaId(int detalleVentaId) {
        this.detalleVentaId = detalleVentaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
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
