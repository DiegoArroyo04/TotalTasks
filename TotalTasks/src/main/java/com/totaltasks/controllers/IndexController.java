package com.totaltasks.controllers;

import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.UsuarioDTO;

import jakarta.servlet.http.HttpSession;


@Controller
public class IndexController {

	@GetMapping("/")
	public String index(HttpSession session, Model model) {
	
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");
	
		// Si existe, lo añadimos al modelo
		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			
			// Convertimos la foto de perfil a Base64 y la añadimos al modelo
			byte[] fotoBytes = usuario.getFotoPerfil();
			if (fotoBytes != null) {
				String fotoPerfilBase64 = Base64.getEncoder().encodeToString(fotoBytes);
				model.addAttribute("fotoPerfilBase64", fotoPerfilBase64);
			}
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
		}

		model.addAttribute("usuario", usuario);

		byte[] fotoBytes = usuario.getFotoPerfil();

		if (fotoBytes != null) {
			String fotoPerfilBase64 = Base64.getEncoder().encodeToString(fotoBytes);
			model.addAttribute("fotoPerfilBase64", fotoPerfilBase64);
		}

		model.addAttribute("paginaActual", "dashboard");

		return "dashboard";
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

		UsuarioDTO dto = new UsuarioDTO();
		dto.setNombre(usuario.getNombre());
		dto.setUsuario(usuario.getUsuario());
		dto.setEmail(usuario.getEmail());
		dto.setContrasenia(usuario.getContrasenia());
		dto.setFotoPerfil(usuario.getFotoPerfil());

		model.addAttribute("usuario", dto);

		return "miPerfil";
	}

}