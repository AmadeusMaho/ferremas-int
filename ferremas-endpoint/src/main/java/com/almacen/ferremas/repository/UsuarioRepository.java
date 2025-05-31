package com.almacen.ferremas.repository;

import com.almacen.ferremas.entity.Usuario;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByTipoUsuario(int tipo);
}
