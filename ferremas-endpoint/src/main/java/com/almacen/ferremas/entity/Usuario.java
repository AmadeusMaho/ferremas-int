package com.almacen.ferremas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuario")

public class Usuario {
    @Id
    private int usuario_id;
    private String username;
    private String clave;
    private String email;
    private int tipo_usuario;

    public Usuario() {
    }

    public Usuario(int usuario_id, String username, String clave, String email, int tipo_usuario) {
        this.usuario_id = usuario_id;
        this.username = username;
        this.clave = clave;
        this.email = email;
        this.tipo_usuario = tipo_usuario;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(int tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }
}
