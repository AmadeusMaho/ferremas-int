package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.DetalleVenta;
import com.almacen.ferremas.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalle_venta")
public class DetalleVentaController {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @GetMapping
    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }

    //Recibir detalle por ID
    @GetMapping("/{id}")
    public DetalleVenta buscarDetallePorId(@PathVariable Integer id) {
        return detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Detalle no encontrado con ID: " + id
                ));
    }

    //Crear detalle
    @PostMapping
    public ResponseEntity<DetalleVenta> crearDetalle(@RequestBody DetalleVenta detalle) {
        if (!detalleVentaRepository.existsById(detalle.getDetalleVentaId())) {
            DetalleVenta nuevoDetalle = detalleVentaRepository.save(detalle);
            return ResponseEntity.ok(nuevoDetalle);
        }
        return null;
    }
}
