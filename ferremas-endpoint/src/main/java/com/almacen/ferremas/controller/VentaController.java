package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Producto;
import com.almacen.ferremas.entity.Venta;
import com.almacen.ferremas.repository.ProductoRepository;
import com.almacen.ferremas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {
    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping
    public List<Venta> listarVentas() {return ventaRepository.findAll();}
    }
