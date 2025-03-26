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

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.services.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/usuarios/")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("registrar")
    public String registrarUsuario(@RequestBody UsuarioDTO usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("registrarGoogle")
    public String registrarUsuarioGoogle(@RequestBody UsuarioDTO usuario, HttpSession session) {

        // Llamamos al servicio para registrar al usuario o iniciar sesión si ya existe
        String respuesta = usuarioService.registrarUsuarioGoogle(usuario);

        // Si el usuario fue encontrado (existe en la base de datos), se agrega a la
        // sesión
        if (respuesta.equals("Usuario encontrado. Iniciando sesión...")) {
            session.setAttribute("usuario", usuarioService.encontrarUsuario(usuario.getUsuario()));
        } else {
            // Si el usuario fue creado, también lo agregamos a la sesión
            session.setAttribute("usuario", usuarioService.encontrarUsuario(usuario.getUsuario()));
        }

        return respuesta;

    }
    
    @GetMapping("githubCallback")
    public void githubCallback(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) throws IOException {
        // Obtener token de acceso
        String accessToken = usuarioService.obtenerAccessTokenDeGitHub(code);
        
        // Obtener datos básicos del usuario
        UsuarioDTO usuarioDTO = usuarioService.obtenerDatosUsuarioGitHub(accessToken);
        
        // Registrar o iniciar sesión
        String respuesta = usuarioService.registrarUsuarioGitHub(usuarioDTO);
        if (respuesta.contains("Iniciando sesión") || respuesta.contains("Cuenta creada")) {
            session.setAttribute("usuario", usuarioService.encontrarUsuario(usuarioDTO.getUsuario()));
        };
        
        // Obtener la lista de repositorios enriquecida
        List<RepoDTO> repositorios = usuarioService.obtenerRepositoriosUsuarioGitHub(accessToken);
        // Aquí podrías, por ejemplo, guardar esta información en la sesión o en la base de datos para usarla en el dashboard
        session.setAttribute("repositorios", repositorios);
        
        // Redirigir al login (o dashboard según tu flujo)
        response.sendRedirect("/login");
    }

    @PostMapping("comprobarLogin")
    public String comprobarLogin(@RequestBody UsuarioDTO usuario, HttpSession session) {

        // SI ENCONTRAMOS EL USUARIO Y ESTE ES VALIDADO LO AÑADIMOS A LA SESSION QUE NO
        // DEVOLVEMOS EL ERROR
        if (usuarioService.comprobarLogin(usuario).equals("Encontrado")) {

            UsuarioEntity usuarioEntity = usuarioService.encontrarUsuario(usuario.getUsuario());

            // Añadimos el usuario a la sesión
            session.setAttribute("usuario", usuarioEntity);

            return "Encontrado";
        } else {
            return usuarioService.comprobarLogin(usuario);
        }

    }

}