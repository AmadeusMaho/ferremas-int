package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Producto;
import com.almacen.ferremas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    //Recibir producto por ID
    @GetMapping("/{id}")
    public Producto buscarProductoPorId(@PathVariable Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));
    }

    //Actualizar Producto por ID
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto ProductoActualizado) {
        Producto ProductoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));

        // Actualizar los campos
        ProductoExistente.setNombre(ProductoActualizado.getNombre());
        ProductoExistente.setDescripcion(ProductoActualizado.getDescripcion());
        ProductoExistente.setPrecio(ProductoActualizado.getPrecio());
        ProductoExistente.setImagen(ProductoActualizado.getImagen());
        ProductoExistente.setStock(ProductoActualizado.getStock());
        ProductoExistente.setDetalles_ventas(ProductoActualizado.getDetalles_ventas());

        // Guardar el Producto actualizado
        Producto ProductoGuardado = productoRepository.save(ProductoExistente);

        return ResponseEntity.ok(ProductoGuardado);
    }

    //Crear Producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto Producto) {
        if (!productoRepository.existsById(Producto.getProducto_id())) {
            Producto nuevoProducto = productoRepository.save(Producto);
            return ResponseEntity.ok(nuevoProducto);
        }
        return null;
    }

    //eliminar Producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarProducto(@PathVariable Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Producto no encontrado con ID: " + id
            );
        }
        productoRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("Mensaje","Producto eliminado");
        response.put("Id Producto",id.toString());
        return ResponseEntity.ok(response);
    }

}
