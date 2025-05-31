package com.almacen.ferremas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.almacen.ferremas.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
