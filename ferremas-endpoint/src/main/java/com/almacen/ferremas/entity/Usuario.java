package com.almacen.ferremas.entity;

import jakarta.persistence.*;

@Entity
@Table(name="usuario")

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer usuarioId;
    private String username;
    private String clave;
    private String email;
    @Column(name = "tipo_usuario")
    private Integer tipoUsuario;

    public Usuario() {
    }

    public Usuario(Integer usuarioId, String username, String clave, String email, Integer tipoUsuario) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.clave = clave;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
