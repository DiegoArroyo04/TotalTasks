package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.totaltasks.models.TareaDTO;
import com.totaltasks.services.TareaService;

@Controller
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping("/crearTarea")
    public String crearTarea(@ModelAttribute TareaDTO tareaDTO) {
        tareaService.crearTarea(tareaDTO);
        return "redirect:/proyecto/" + tareaDTO.getIdProyecto();
    }

}