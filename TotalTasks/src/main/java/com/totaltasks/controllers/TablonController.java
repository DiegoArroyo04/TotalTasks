package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.models.TablonDTO;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.TablonService;

@RestController
public class TablonController {
	@Autowired
	TablonService tablonService;

	@Autowired
	ProyectoService proyectoService;

	@PostMapping("/crearTablon")
	public Long crearTablon(@RequestBody TablonDTO tablon) {
		tablon.setProyecto(proyectoService.obtenerProyectoPorId(tablon.getId_proyecto()));
		return tablonService.crearTablon(tablon);
	}

}