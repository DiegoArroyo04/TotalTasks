package com.totaltasks.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.services.TareaService;
import com.totaltasks.services.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/usuarios/")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	TareaService tareaService;

	@PostMapping("registrar")
	public String registrarUsuario(@RequestBody UsuarioDTO usuario) {
		return usuarioService.registrarUsuario(usuario);
	}

	@PostMapping("registrarGoogle")
	public String registrarUsuarioGoogle(@RequestBody UsuarioDTO usuario, HttpSession session) {

		// Llamamos al servicio para registrar al usuario o iniciar sesión si ya existe
		String respuesta = usuarioService.registrarUsuarioGoogle(usuario);

		// Si el usuario fue encontrado se agrega a la sesión
		if (respuesta.equals("Usuario encontrado. Iniciando sesión...")) {
			UsuarioEntity usuarioEntity = usuarioService.encontrarUsuario(usuario.getEmail());
			session.setAttribute("usuario", usuarioEntity);

			// Cada vez que se loguee el usuario verificar si tiene tareas a punto de finalizar
			tareaService.verificarTareasProximas(usuarioEntity);

			session.setAttribute("fotoPerfilGoogle", usuario.getFotoPerfilGoogle());
		} else {
			// Si el usuario fue creado, también lo agregamos a la sesión
			session.setAttribute("usuario", usuarioService.encontrarUsuario(usuario.getEmail()));
			session.setAttribute("fotoPerfilGoogle", usuario.getFotoPerfilGoogle());
		}

		return respuesta;

	}

	// REGISTRO GITHUB
	@GetMapping("githubCallback")
	public void githubCallback(@RequestParam(value = "code", required = false) String code, HttpServletResponse response,
	@RequestParam(value = "error", required = false) String error, HttpSession session) throws IOException {

		// Si el usuario cancela los permisos redireccion al lobby
		if (error != null && error.equals("access_denied")) {
			response.sendRedirect("/login");
			return;
		}

		// Obtener token de acceso
		String accessToken = usuarioService.obtenerAccessTokenDeGitHub(code);

		// Obtener datos básicos del usuario
		UsuarioDTO usuarioDTO = usuarioService.obtenerDatosUsuarioGitHub(accessToken);

		// Registrar o iniciar sesión
		String respuesta = usuarioService.registrarUsuarioGitHub(usuarioDTO);
		if (respuesta.contains("Iniciando sesión") || respuesta.contains("Cuenta creada")) {
			UsuarioEntity usuarioEntity = usuarioService.encontrarUsuario(usuarioDTO.getEmail());
			session.setAttribute("usuario", usuarioEntity);
			// Cada vez que se loguee el usuario verificar si tiene tareas a punto de finalizar
			tareaService.verificarTareasProximas(usuarioEntity);
		}

		// Obtener la lista de repositorios
		List<RepoDTO> repositorios = usuarioService.obtenerRepositoriosUsuarioGitHub(accessToken);

		// Guardar información en la sesión
		session.setAttribute("access_token", accessToken);
		session.setAttribute("repositorios", repositorios);
		session.setAttribute("fotoPerfilGithub", usuarioDTO.getFotoPerfilGithub());

		// Redirigir al login
		response.sendRedirect("/login");
	}

	@PostMapping("comprobarLogin")
	public String comprobarLogin(@RequestBody UsuarioDTO usuario, HttpSession session) {

		// Si encontramos al usuario y es valido lo añadimos a la sesion
		if (usuarioService.comprobarLogin(usuario).equals("Encontrado")) {

			UsuarioEntity usuarioEntity = usuarioService.encontrarUsuarioPorUsuario(usuario.getUsuario());

			// Añadimos el usuario a la sesión
			session.setAttribute("usuario", usuarioEntity);

			// Cada vez que se loguee el usuario verificar si tiene tareas a punto de finalizar
			tareaService.verificarTareasProximas(usuarioEntity);

			return "Encontrado";
		} else {
			return usuarioService.comprobarLogin(usuario);
		}

	}

	// Método para configuarar mi perfil
	@GetMapping("miCuenta")
	public void miCuenta(HttpServletResponse response) throws Exception {
		response.sendRedirect("/editarPerfil");
	}

	// Metodo con la funcionalidad del perfil actualizado
	@PostMapping("perfilEditado")
	public void perfilEditado(HttpServletResponse response, @RequestParam("nombre") String nombre, @RequestParam(value = "fotoPerfil", required = false)
	MultipartFile fotoPerfil, HttpSession session) throws Exception {

		UsuarioEntity usuario = (UsuarioEntity) session.getAttribute("usuario");

		if (usuario == null) {
			response.sendRedirect("/login");
			return;
		}

		usuarioService.actualizarUsuario(nombre, fotoPerfil, usuario);
		session.setAttribute("usuario", usuario);

		response.sendRedirect("/usuarios/miCuenta");
	}

	// Método para cerrar sesión
	@GetMapping("cerrarSesion")
	public void cerrarSesion(HttpServletResponse response, HttpSession session) throws IOException {
		session.invalidate();
		response.sendRedirect("/login");
	}

}