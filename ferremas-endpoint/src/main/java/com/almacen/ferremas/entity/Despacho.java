package com.almacen.ferremas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Entity
@Table(name="despacho")

public class Despacho {
    @Id
    private int despacho_id;
    private Date fecha_despacho;
    private Date fecha_recibido;

    @ManyToOne
    @JoinColumn(name="direccion_id", nullable = false)
    @JsonIgnoreProperties("direccion")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name="venta_id", nullable = false)
    @JsonIgnoreProperties("venta")
    private Venta venta;

    public Despacho() {
    }

    public Despacho(int despacho_id, Date fecha_despacho, Date fecha_recibido, Direccion direccion, Venta venta) {
        this.despacho_id = despacho_id;
        this.fecha_despacho = fecha_despacho;
        this.fecha_recibido = fecha_recibido;
        this.direccion = direccion;
        this.venta = venta;
    }

    public int getDespacho_id() {
        return despacho_id;
    }

    public void setDespacho_id(int despacho_id) {
        this.despacho_id = despacho_id;
    }

    public Date getFecha_despacho() {
        return fecha_despacho;
    }

    public void setFecha_despacho(Date fecha_despacho) {
        this.fecha_despacho = fecha_despacho;
    }

    public Date getFecha_recibido() {
        return fecha_recibido;
    }

    public void setFecha_recibido(Date fecha_recibido) {
        this.fecha_recibido = fecha_recibido;
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
