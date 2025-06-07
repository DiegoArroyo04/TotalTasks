package com.totaltasks.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.UsuarioProyectoEntity;
import com.totaltasks.repositories.UsuarioProyectoRepository;
import com.totaltasks.services.NotificacionUsuarioService;
import com.totaltasks.services.ProyectoService;

import com.totaltasks.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

	@Autowired
	UsuarioProyectoRepository usuarioProyectoRepository;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ProyectoService proyectoService;

	@Autowired
	NotificacionUsuarioService notificacionUsuarioService;

	@GetMapping("/")
	public String index(HttpSession session, Model model) {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		// Si existe, lo añadimos al modelo
		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("notificacionesNoLeidas",
					notificacionUsuarioService.notificacionesNoLeidasPorUserId(usuario.getIdUsuario()));
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

		if (usuario == null) {
			return "redirect:/login";
		} else {

			model.addAttribute("usuario", usuario);
			model.addAttribute("notificacionesNoLeidas",
					notificacionUsuarioService.notificacionesNoLeidasPorUserId(usuario.getIdUsuario()));
			model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));
			model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
			model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));
			model.addAttribute("paginaActual", "dashboard");

			List<ProyectoEntity> proyectos = proyectoService.todosLosProyectosDeUnUsuario(usuario);
			model.addAttribute("proyectos", proyectos);

			// Obtener colores
			List<UsuarioProyectoEntity> usuarioProyectos = usuarioProyectoRepository.findByUsuario(usuario);

			// Mapeamos idProyecto → colores
			Map<Long, String> coloresPrimarios = new HashMap<>();
			Map<Long, String> coloresHover = new HashMap<>();

			for (UsuarioProyectoEntity up : usuarioProyectos) {
				coloresPrimarios.put(up.getProyecto().getIdProyecto(), up.getColorPrimario());
				coloresHover.put(up.getProyecto().getIdProyecto(), up.getColorHover());
			}

			model.addAttribute("coloresPrimarios", coloresPrimarios);
			model.addAttribute("coloresHover", coloresHover);

			return "dashboard";
		}
	}

	@GetMapping("/privacidad")
	public String privacidad(HttpSession session, Model model) {
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("notificacionesNoLeidas",
					notificacionUsuarioService.notificacionesNoLeidasPorUserId(usuario.getIdUsuario()));
			model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));
			model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
			model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));
			model.addAttribute("paginaActual", "text-legal");
		}

		return "/textos-legales/privacidad";
	}

	@GetMapping("/terminos-de-uso")
	public String terminos(HttpSession session, Model model) {
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("notificacionesNoLeidas",
					notificacionUsuarioService.notificacionesNoLeidasPorUserId(usuario.getIdUsuario()));
			model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));
			model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
			model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));
			model.addAttribute("paginaActual", "text-legal");
		}
		return "/textos-legales/terminos-de-uso";
	}

	@GetMapping("/faq")
	public String faq(HttpSession session, Model model) {
		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario != null) {
			model.addAttribute("usuario", usuario);
			model.addAttribute("notificacionesNoLeidas",
					notificacionUsuarioService.notificacionesNoLeidasPorUserId(usuario.getIdUsuario()));
			model.addAttribute("fotoPerfilBase64", usuarioService.convertirByteABase64(usuario.getFotoPerfil()));
			model.addAttribute("fotoperfilGoogle", (String) session.getAttribute("fotoPerfilGoogle"));
			model.addAttribute("fotoPerfilGithub", (String) session.getAttribute("fotoPerfilGithub"));
			model.addAttribute("paginaActual", "text-legal");
		}
		return "/informacion/faq";
	}

	@GetMapping("/formulariosGithub")
	public String formulariosGithub(HttpSession session, Model model) {
		String accessToken = (String) session.getAttribute("access_token");
		if (accessToken == null) {
			model.addAttribute("notLoggedIn", true);
		}
		return "formulariosGithub";
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