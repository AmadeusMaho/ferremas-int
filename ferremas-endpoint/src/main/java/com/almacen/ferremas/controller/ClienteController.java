package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Cliente;
import com.almacen.ferremas.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }


    //Recibir cliente por ID
    @GetMapping("/{id}")
    public Cliente buscarClientePorId(@PathVariable Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id
                ));
    }

    //Actualizar cliente por ID
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id
                ));

        // Actualizar los campos
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setRun(clienteActualizado.getRun());
        clienteExistente.setDv((clienteActualizado.getDv()));
        clienteExistente.setVentas(clienteActualizado.getVentas());
        clienteExistente.setDirecciones(clienteActualizado.getDirecciones());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        // Guardar el cliente actualizado
        Cliente clienteGuardado = clienteRepository.save(clienteExistente);

        return ResponseEntity.ok(clienteGuardado);
    }

    //Crear cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        if (!clienteRepository.existsById(cliente.getCliente_id())) {
            Cliente nuevoCliente = clienteRepository.save(cliente);
            return ResponseEntity.ok(nuevoCliente);
        }
        return null;
    }

    //eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarCliente(@PathVariable Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cliente no encontrado con ID: " + id
            );
        }
        clienteRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("Mensaje","Cliente eliminado");
        response.put("Id Cliente",id.toString());
        return ResponseEntity.ok(response);
    }


}
