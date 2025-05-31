package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Direccion;
import com.almacen.ferremas.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/direccion")
public class DireccionController {
    @Autowired
    private DireccionRepository direccionRepository;

    @GetMapping
    public List<Direccion> listarDirecciones() {return direccionRepository.findAll();}
    }
