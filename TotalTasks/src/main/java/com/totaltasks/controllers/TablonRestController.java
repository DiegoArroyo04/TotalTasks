package com.totaltasks.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.models.UsuarioProyectoDTO;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.TablonService;

import jakarta.servlet.http.HttpSession;

// TablonRestController.java
@RestController
public class TablonRestController {

	@Autowired
	private TablonService tablonService;

	@Autowired
	private ProyectoService proyectoService;

	@PostMapping("/actualizarOrdenTablones")
	public String actualizarOrden(@RequestBody List<TablonDTO> ordenes, HttpSession session) {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario != null) {
			return tablonService.actualizarOrdenTablones(ordenes, usuario);
		} else {
			return tablonService.actualizarOrdenTablones(ordenes, null);
		}

	}

	@PostMapping("/eliminarTablon")
	public String eliminarTablon(@RequestBody TablonDTO tablon, HttpSession session) {
		ProyectoEntity proyecto = proyectoService.obtenerProyectoPorId(tablon.getId_proyecto());
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");
		if (proyecto != null) {
			if (usuario != null) {
				return tablonService.eliminarTablon(proyecto, tablon.getNombreTablon(), usuario);
			} else {
				return tablonService.eliminarTablon(proyecto, tablon.getNombreTablon(), null);
			}
		} else {
			return "Proyecto no encontrado";
		}
	}

	@PostMapping("/crearTablon")
	public String crearTablon(@RequestBody TablonDTO tablon, HttpSession session) {

		tablon.setProyecto(proyectoService.obtenerProyectoPorId(tablon.getId_proyecto()));

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		Long respuesta;

		if (usuario != null) {
			respuesta = tablonService.crearTablon(tablon, usuario);
		} else {
			respuesta = tablonService.crearTablon(tablon, null);
		}

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

	@GetMapping("/obtenerColores")
	public UsuarioProyectoDTO obtenerColores(@RequestParam Long usuarioId, @RequestParam Long proyectoId) {
		return tablonService.obtenerColores(usuarioId, proyectoId);
	}

	@PostMapping("/actualizarNombreTablon")
	public String actualizarNombreTablon(@RequestBody TablonDTO tablonDTO, HttpSession session) {
		Long id = tablonDTO.getId();
		String nuevoNombre = tablonDTO.getNombreTablon();

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		boolean actualizado;

		if (usuario != null) {
			actualizado = tablonService.actualizarNombreTablon(id, nuevoNombre, usuario);
		} else {
			actualizado = tablonService.actualizarNombreTablon(id, nuevoNombre, null);
		}

		if (actualizado) {
			return "Nombre actualizado correctamente";
		} else {
			return "No se pudo actualizar (tablero no encontrado o nombre duplicado)";
		}
	}

}