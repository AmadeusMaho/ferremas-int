package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.DetalleVenta;
import com.almacen.ferremas.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/detalle_venta")
public class DetalleVentaController {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @GetMapping
    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }
}
