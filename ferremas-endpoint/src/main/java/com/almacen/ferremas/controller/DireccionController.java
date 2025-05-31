package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Direccion;
import com.almacen.ferremas.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/direccion")
public class DireccionController {
    @Autowired
    private DireccionRepository direccionRepository;

    @GetMapping
    public List<Direccion> listarDirecciones() {return direccionRepository.findAll();}

    //Recibir direccion por ID
    @GetMapping("/{id}")
    public Direccion buscarDireccionPorId(@PathVariable Integer id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Dirección no encontrada con ID: " + id
                ));
    }

    //Actualizar direccion por ID
    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizarDireccion(@PathVariable Integer id, @RequestBody Direccion direccionActualizada) {
        Direccion direccionExistente = direccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Dirección no encontrada con ID: " + id
                ));

        // Actualizar los campos
        direccionExistente.setNombre(direccionActualizada.getNombre());
        direccionExistente.setDescripcion(direccionActualizada.getDescripcion());

        // Guardar la dirección actualizada
        Direccion direccionGuardada = direccionRepository.save(direccionExistente);

        return ResponseEntity.ok(direccionGuardada);
    }

    //Crear direccion
    @PostMapping
    public ResponseEntity<Direccion> crearDireccion(@RequestBody Direccion direccion) {
        if (!direccionRepository.existsById(direccion.getDireccionId())) {
            Direccion nuevaDireccion = direccionRepository.save(direccion);
            return ResponseEntity.ok(nuevaDireccion);
        }
        return null;
    }

    //Eliminar direccion
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarDireccion(@PathVariable Integer id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Dirección no encontrada con ID: " + id
            );
        }
        direccionRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("Mensaje","Dirección eliminada");
        response.put("Id Dirección",id.toString());
        return ResponseEntity.ok(response);
    }
}

