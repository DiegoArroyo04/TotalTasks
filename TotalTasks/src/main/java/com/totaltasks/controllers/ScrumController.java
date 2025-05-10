package com.totaltasks.controllers;

import com.totaltasks.models.ProductBacklogDTO;
import com.totaltasks.services.ScrumService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ScrumController {


    @Autowired
	private ScrumService scrumService;

    @PostMapping("/scrum/agregarHistoria")
    public void agregarHistoria(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam Integer storyPoints, @RequestParam Long idProyecto,
    @RequestParam Long idUsuario, HttpServletResponse response) throws IOException {
        ProductBacklogDTO backlogDTO = new ProductBacklogDTO(titulo, descripcion, storyPoints, idProyecto, idUsuario);
        scrumService.agregarHistoria(backlogDTO);
        response.sendRedirect("/proyecto/" + idProyecto);
    }
}
