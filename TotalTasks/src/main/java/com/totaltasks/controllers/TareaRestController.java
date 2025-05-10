package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.TareaDTO;
import com.totaltasks.services.TareaService;

import jakarta.servlet.http.HttpSession;

@RestController
public class TareaRestController {
	@Autowired
	private TareaService tareaService;

	@PostMapping("/actualizarEstadoTarea")
	public String crearTarea(@RequestBody TareaDTO tarea, HttpSession session) {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario != null) {
			return tareaService.modificarEstadoTarea(tarea, usuario);
		} else {
			return tareaService.modificarEstadoTarea(tarea, null);
		}

	}
}