package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Cliente;
import com.almacen.ferremas.entity.Despacho;
import com.almacen.ferremas.entity.DetalleVenta;
import com.almacen.ferremas.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/despacho")
public class DespachoController {
    @Autowired
    private DespachoRepository despachoRepository;

    @GetMapping
    public List<Despacho> listarDespachos() {return despachoRepository.findAll();}

    //Recibir despacho por ID
    @GetMapping("/{id}")
    public Despacho buscarDespachoPorId(@PathVariable Integer id) {
        return despachoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Despacho no encontrado con ID: " + id
                ));
    }

    //Crear despacho
    @PostMapping
    public ResponseEntity<Despacho> crearDespacho(@RequestBody Despacho despacho) {
        if (!despachoRepository.existsById(despacho.getDespachoId())) {
            Despacho nuevoDespacho = despachoRepository.save(despacho);
            return ResponseEntity.ok(nuevoDespacho);
        }
        return null;
    }

    //Actualizar despacho por ID
    @PutMapping("/{id}")
    public ResponseEntity<Despacho> actualizarDespacho(@PathVariable Integer id, @RequestBody Despacho despachoActualizado) {
        Despacho despachoExistente = despachoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Despacho no encontrado con ID: " + id
                ));

        // Actualizar los campos
        despachoExistente.setFechaDespacho(despachoActualizado.getFechaDespacho());
        despachoExistente.setFechaRecibido(despachoActualizado.getFechaRecibido());
        despachoExistente.setDireccion(despachoActualizado.getDireccion());

        // Guardar el despacho actualizado
        Despacho despachoGuardado = despachoRepository.save(despachoExistente);

        return ResponseEntity.ok(despachoGuardado);
    }
}

