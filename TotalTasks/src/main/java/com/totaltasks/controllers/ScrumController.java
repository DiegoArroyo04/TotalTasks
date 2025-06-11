package com.totaltasks.controllers;

import com.totaltasks.models.ProductBacklogDTO;
import com.totaltasks.services.ScrumService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@PostMapping("/scrum/moverHistoriaASprint")
	public String moverHistoriaASprint(@RequestParam Long idBacklog, @RequestParam Long idProyecto, @RequestParam Long idResponsable) {
		scrumService.moverHistoriaASprint(idBacklog, idProyecto, idResponsable);
		return "Historia movida a Sprint";
	}

	@PostMapping("/scrum/moverDeSprintABacklog")
	public String moverHistoriaDeSprintABacklog(@RequestParam Long idSprint, @RequestParam Long idProyecto, @RequestParam Long idResponsable) {
		scrumService.moverHistoriaDeSprintABacklog(idSprint, idProyecto, idResponsable);
		return "Historia movida de Sprint al Product Backlog";
	}

	@PostMapping("/scrum/comenzarSprint")
	public ResponseEntity<String> comenzarSprint(@RequestParam Long idProyecto, @RequestParam double duracionDias) {
		scrumService.comenzarSprint(idProyecto, duracionDias);
		return ResponseEntity.ok("Sprint iniciado");
	}

	@GetMapping("/scrum/sprintTerminado")
	public boolean sprintTerminado(@RequestParam Long idProyecto) {
		return scrumService.estaTerminado(idProyecto);
	}

	@PostMapping("/scrum/actualizarEstadoTarea")
	public void actualizarEstadoTarea(@RequestParam Long idTarea, @RequestParam String nuevoEstado) {
		scrumService.actualizarEstadoTarea(idTarea, nuevoEstado);
	}

	@PostMapping("/scrum/borrarTareasHechas")
	public void borrarTareasHechas() {
		scrumService.borrarTareasHechas();
	}

}