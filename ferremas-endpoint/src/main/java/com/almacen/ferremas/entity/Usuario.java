package com.almacen.ferremas.entity;

import jakarta.persistence.*;

@Entity
@Table(name="usuario")

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private int usuarioId;
    private String username;
    private String clave;
    private String email;
    @Column(name = "tipo_usuario")
    private int tipoUsuario;

    public Usuario() {
    }

    public Usuario(int usuarioId, String username, String clave, String email, int tipoUsuario) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.clave = clave;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
