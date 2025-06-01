package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Usuario;
import com.almacen.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {return usuarioRepository.findAll();}

    @GetMapping("/{id}")
    public Usuario buscarUsuarioPorId(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id
                ));
    }

    @GetMapping("/tipo/{tipo}")
    public List<Usuario> buscarUsuarioPorTipo(@PathVariable Integer tipo){
        List<Usuario> usuariosTipo = usuarioRepository.findByTipoUsuario(tipo);
        if (usuariosTipo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No se encontraron usuarios del tipo: " + tipo
            );
        }
        return usuariosTipo;
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            usuario.setUsuarioId(null); // ensure new entity
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado con ID: " + id
            );
        }
        usuarioRepository.deleteById(id);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("Mensaje","Usuario eliminado");
        respuesta.put("Id Usuario",id.toString());
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id
                ));

        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setClave(usuarioActualizado.getClave());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTipoUsuario(usuarioActualizado.getTipoUsuario());

        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);

        return ResponseEntity.ok(usuarioGuardado);
    }

}
