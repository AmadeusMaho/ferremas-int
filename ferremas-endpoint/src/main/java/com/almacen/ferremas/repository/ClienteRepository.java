package com.almacen.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.almacen.ferremas.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
