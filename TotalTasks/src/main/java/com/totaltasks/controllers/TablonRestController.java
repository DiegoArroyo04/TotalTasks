package com.totaltasks.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.models.TablonDTO;
import com.totaltasks.services.TablonService;

// TablonRestController.java
@RestController
public class TablonRestController {

    @Autowired
    private TablonService tablonService;

    @PostMapping("/actualizarOrdenTablones")
    public String actualizarOrden(@RequestBody List<TablonDTO> ordenes) {
        return tablonService.actualizarOrdenTablones(ordenes);
    }
}