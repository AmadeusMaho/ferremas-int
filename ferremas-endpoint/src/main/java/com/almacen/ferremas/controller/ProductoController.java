package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Producto;
import com.almacen.ferremas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }
}
