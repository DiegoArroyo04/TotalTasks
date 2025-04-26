package com.totaltasks.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.models.TablonDTO;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.TablonService;

// TablonRestController.java
@RestController
public class TablonRestController {

    @Autowired
    private TablonService tablonService;

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping("/actualizarOrdenTablones")
    public String actualizarOrden(@RequestBody List<TablonDTO> ordenes) {
        return tablonService.actualizarOrdenTablones(ordenes);
    }

    @PostMapping("/eliminarTablon")
    public String eliminarTablon(@RequestBody TablonDTO tablon) {

        return "";
    }

}