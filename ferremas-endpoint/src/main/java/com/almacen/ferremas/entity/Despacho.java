package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Entity
@Table(name="despacho")

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
    @JsonBackReference
    @JoinColumn(name="direccion_id", nullable = false)
    //@JsonIgnoreProperties("direccion")
    private Direccion direccion;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="venta_id", nullable = false)
    //@JsonIgnoreProperties("venta")
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

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Date getFechaRecibido() {
        return fechaRecibido;
    }

    public void setFechaRecibido(Date fechaRecibido) {
        this.fechaRecibido = fechaRecibido;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public int getDespachoId() {
        return despachoId;
    }

    public void setDespachoId(int despachoId) {
        this.despachoId = despachoId;
    }
}
