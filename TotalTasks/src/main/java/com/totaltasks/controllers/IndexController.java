package com.totaltasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.totaltasks.entities.UsuarioEntity;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

	@GetMapping("/")
	public String index(HttpSession session, Model model) {

		// SI EL USUARIO YA ESTA LOGUEADO QUE LE Apareza la opcion de mi dashboard
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			return "index";
		} else {
			model.addAttribute("usuario", usuario);
			return "index";
		}

	}

	@GetMapping("/registro")
	public String registro() {
		return "registro";
	}

	@GetMapping("/login")
	public String login(HttpSession session) {

		// SI EL USUARIO YA ESTA LOGUEADO QUE LE REDIRIJA AL DASHBOARD Y NO SE TENGA QUE
		// VOLVER A LOGUEAR
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			return "login";
		} else {
			return "redirect:/dashboard";
		}

	}

	@GetMapping("/dashboard")
	public String dashboard(HttpSession session) {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			return "redirect:/login";
		} else {
			return "dashboard";
		}

	}

}