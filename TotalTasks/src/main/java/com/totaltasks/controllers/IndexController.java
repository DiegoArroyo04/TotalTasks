package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ProyectoService proyectoService;

	@GetMapping("/")
	public String index(HttpSession session, Model model) {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		// Si existe, lo añadimos al modelo
		if (usuario != null) {
			model.addAttribute("usuario", usuario);

			// Convertimos la foto de perfil a Base64 y la añadimos al modelo
			model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));
			// FOTO DE PERFIL DE GOOGLE Y GITHUB
			model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
			model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));
		}

		model.addAttribute("paginaActual", "index");
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
	public String dashboard(HttpSession session, Model model) {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		// Si no hay usuario, redirigimos al login
		if (usuario == null) {
			return "redirect:/login";
		} else {

			model.addAttribute("usuario", usuario);
			model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));

			// FOTO DE PERFIL DE GOOGLE Y GITHUB
			model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
			model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));

			model.addAttribute("paginaActual", "dashboard");

			model.addAttribute("proyectos", proyectoService.todosLosProyectosDeUnUsuario(usuario));

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

	@GetMapping("/editarPerfil")
	public String editarPerfil(HttpSession session, Model model) {
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			return "redirect:/login";
		}

		model.addAttribute("usuario", usuario);
		// FOTO DE PERFIL DE GOOGLE Y GITHUB
		model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));
		model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
		model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));

		return "miPerfil";
	}

}