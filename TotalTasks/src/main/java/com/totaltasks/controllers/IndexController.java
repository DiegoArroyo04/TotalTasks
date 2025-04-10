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

		// Si el usuario ya esta logueado que le aparezca la opcion de "Mi dashboard"
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");
		System.out.println(usuario);

		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			System.out.println("ID del usuario: " + usuario.getIdUsuario());
		} else {
			System.out.println("No hay usuario en sesión.");
		}
		return "index";

	}

	@GetMapping("/registro")
	public String registro() {
		return "registro";
	}

	@GetMapping("/login")
	public String login(HttpSession session) {

		// Si el usuario ya esta logueado que le redirija al dashboard
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			return "login";
		} else {
			return "redirect:/dashboard";
		}

	}

	@GetMapping("/dashboard")
	public String dashboard(HttpSession session) {

		// Si no encuentra la sesion del usuario se le redirije al login
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			return "redirect:/login";
		} else {
			return "dashboard";
		}

	}

	@GetMapping("/privacidad")
	public String privacidad() {
		return "/textos-legales/privacidad";
	}

	@GetMapping("/terminos-de-uso")
	public String terminos() {
		return "/textos-legales/terminos-de-uso";
	}
	
	@GetMapping("/documentacion")
	public String documentacion() {
		return "/informacion/documentacion";
	}

	@GetMapping("/faq")
	public String faq() {
		return "/informacion/faq";
	}

	@GetMapping("/formulariosGithub")
	public String formulariosGithub() {
		return "/formulariosGithub";
	}

}