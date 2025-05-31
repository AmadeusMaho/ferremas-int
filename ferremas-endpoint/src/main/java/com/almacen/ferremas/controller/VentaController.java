package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.*;
import com.almacen.ferremas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {
    @Autowired
    private VentaRepository ventaRepository;

    //Recibir ventas por ID
    @GetMapping
    public List<Venta> listarVentas() {return ventaRepository.findAll();}
    @GetMapping("/{id}")
    public Venta buscarVentaPorId(@PathVariable Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Venta no encontrada con ID: " + id
                ));
    }

    //Recibir detalles por ID de la venta
    @GetMapping("/{id}/detalle")
    public List<DetalleVenta> buscarDetalles(@PathVariable Integer id){
        Venta venta = buscarVentaPorId(id);
        List<DetalleVenta> detalleVentas = venta.getDetallesVentas();
        if (detalleVentas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron detalles para la venta con ID: " + id
            );
        }
        return detalleVentas;
    }

    //Recibir despachos asociados a una venta
    @GetMapping("/{id}/despacho")
    public List<Despacho> buscarDespachos(@PathVariable Integer id){
        Venta venta = buscarVentaPorId(id);
        List<Despacho> despachoVenta = venta.getDespachos();
        if (despachoVenta.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron despachos para la venta con ID: " + id
            );
        }
        return despachoVenta;
    }

    @PostMapping
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta venta) {
        if (!ventaRepository.existsById(venta.getVentaId())) {
            Venta ventaGuardada = ventaRepository.save(venta);
            return ResponseEntity.ok(ventaGuardada);
        }
        return null;
    }
}
