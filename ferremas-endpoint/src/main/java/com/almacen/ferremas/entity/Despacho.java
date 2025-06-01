package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "despacho")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "despacho_id")
    private int despachoId;

    @Column(name = "fecha_despacho")
    private Date fechaDespacho;

    @Column(name = "fecha_recibido")
    private Date fechaRecibido;

    @ManyToOne
    @JoinColumn(name = "direccion_id", nullable = false)
    @JsonIgnoreProperties({"despachos", "cliente"})
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnoreProperties({"detallesVentas", "despachos", "cliente"})
    private Venta venta;

    public Despacho() {
    }

    public Despacho(int despachoId, Date fechaDespacho, Date fechaRecibido, Direccion direccion, Venta venta) {
        this.despachoId = despachoId;
        this.fechaDespacho = fechaDespacho;
        this.fechaRecibido = fechaRecibido;
        this.direccion = direccion;
        this.venta = venta;
    }

    public int getDespachoId() {
        return despachoId;
    }

    public void setDespachoId(int despachoId) {
        this.despachoId = despachoId;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public Date getFechaRecibido() {
        return fechaRecibido;
    }

    public void setFechaRecibido(Date fechaRecibido) {
        this.fechaRecibido = fechaRecibido;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
