package com.totaltasks.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.entities.TareaEntity;
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

	@GetMapping(value = "/obtenerTareasPorUserYProyecto", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TareaEntity> obtenerTareasPorUserYProyecto(@RequestParam Long usuarioId, @RequestParam Long proyectoId) {
		return tareaService.obtenerTareasPorUserYProyecto(usuarioId, proyectoId);
	}

	@PostMapping("/cambiarFechaTarea")
	public void obtenerTareasPorUserYProyecto(@RequestParam Long idUsuario, Long idTarea, @RequestParam Date nuevaFecha) {
		tareaService.actualizarFechaTarea(idTarea, nuevaFecha, idUsuario);
	}

	@PostMapping("/eliminarTarea/{idTarea}/{idUsuario}/{idProyecto}")
	public void eliminarTarea(@PathVariable Long idTarea, @PathVariable Long idUsuario, @PathVariable Long idProyecto) {
		tareaService.deleteById(idTarea, idUsuario, idProyecto);
	}

}