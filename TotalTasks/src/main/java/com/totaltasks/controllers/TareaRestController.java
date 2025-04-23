package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.models.TareaDTO;
import com.totaltasks.services.TareaService;

@RestController
public class TareaRestController {
    @Autowired
    private TareaService tareaService;

    @PostMapping("/actualizarEstadoTarea")
    public String crearTarea(@RequestBody TareaDTO tarea) {
        return tareaService.modificarEstadoTarea(tarea);
    }
}
