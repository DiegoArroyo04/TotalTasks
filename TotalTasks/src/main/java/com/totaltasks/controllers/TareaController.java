package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.totaltasks.models.TareaDTO;
import com.totaltasks.services.TareaService;

@Controller
public class TareaController {

	@Autowired
	private TareaService tareaService;

	@PostMapping("/crearTarea")
	public String crearTarea(@ModelAttribute TareaDTO tareaDTO, Model model) {
		boolean creada = tareaService.crearTarea(tareaDTO);

		if (creada) {
			return "redirect:/proyecto/" + tareaDTO.getIdProyecto();
		} else {
			model.addAttribute("error", "No se pudo crear la tarea porque no hay ningún tablón en el proyecto.");
			return "redirect:/proyecto/" + tareaDTO.getIdProyecto();
		}
	}

	@PostMapping("/editarTarea")
	public String editarTarea(@ModelAttribute TareaDTO tareaDTO) {
		tareaService.actualizarTarea(tareaDTO);
		return "redirect:/proyecto/" + tareaDTO.getIdProyecto();
	}

}