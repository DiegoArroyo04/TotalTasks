package com.totaltasks.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.models.UsuarioProyectoDTO;
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
		ProyectoEntity proyecto = proyectoService.obtenerProyectoPorId(tablon.getId_proyecto());
		if (proyecto != null) {
			return tablonService.eliminarTablon(proyecto, tablon.getNombreTablon());
		} else {
			return "Proyecto no encontrado";
		}
	}

	@PostMapping("/crearTablon")
	public String crearTablon(@RequestBody TablonDTO tablon) {
		tablon.setProyecto(proyectoService.obtenerProyectoPorId(tablon.getId_proyecto()));

		Long respuesta = tablonService.crearTablon(tablon);
		if (respuesta == null) {
			return "Duplicado";
		} else {
			return respuesta.toString();
		}

	}

	@PostMapping("/guardarColores")
	public void guardarColores(@RequestBody UsuarioProyectoDTO usuarioProyectoDTO) {
		tablonService.guardarColores(usuarioProyectoDTO);

	}

}