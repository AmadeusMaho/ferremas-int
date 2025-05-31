package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="venta")

public class Venta {
    @Id
    private int venta_id;
    private Date fecha;
    private String medio_pago;

    @ManyToOne
    @JoinColumn(name="cliente_id", nullable = false)
    @JsonIgnoreProperties("cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Despacho> despachos;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles_ventas;

    public Venta() {
    }

    public Venta(int venta_id, Date fecha, String medio_pago, Cliente cliente, List<Despacho> despachos, List<DetalleVenta> detalles_ventas) {
        this.venta_id = venta_id;
        this.fecha = fecha;
        this.medio_pago = medio_pago;
        this.cliente = cliente;
        this.despachos = despachos;
        this.detalles_ventas = detalles_ventas;
    }

    public int getVenta_id() {
        return venta_id;
    }

    public void setVenta_id(int venta_id) {
        this.venta_id = venta_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMedio_pago() {
        return medio_pago;
    }

    public void setMedio_pago(String medio_pago) {
        this.medio_pago = medio_pago;
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

    public List<DetalleVenta> getDetalles_ventas() {
        return detalles_ventas;
    }

    public void setDetalles_ventas(List<DetalleVenta> detalles_ventas) {
        this.detalles_ventas = detalles_ventas;
    }
}

