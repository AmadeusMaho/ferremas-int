package com.almacen.ferremas.controller;

import com.almacen.ferremas.entity.Despacho;
import com.almacen.ferremas.repository.DespachoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/despacho")
public class DespachoController {
    @Autowired
    private DespachoRepository despachoRepository;

    @GetMapping
    public List<Despacho> listarDespachos() {return despachoRepository.findAll();}
    }
