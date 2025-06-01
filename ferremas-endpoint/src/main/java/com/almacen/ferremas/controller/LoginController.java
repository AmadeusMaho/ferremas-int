package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.JwtUtil;
import com.almacen.ferremas.entity.Usuario;
import com.almacen.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String clave = request.get("clave");

        Optional<Usuario> usuarioOpcional = userRepository.findByUsername(username);

        if (usuarioOpcional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave o usuario invalidos");
        }

        Usuario user = usuarioOpcional.get();

        if (!user.getClave().equals(clave)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave o usuario invalidos");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("tipo_usuario", user.getTipoUsuario());

        String token = jwtUtil.generateToken(user.getUsername(), claims);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("tipo_usuario", user.getTipoUsuario());
        response.put("email", user.getEmail());
        response.put("usuarioId", user.getUsuarioId());
        response.put("clave", user.getClave());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logged out. Please delete your token on client.");
    }
}
