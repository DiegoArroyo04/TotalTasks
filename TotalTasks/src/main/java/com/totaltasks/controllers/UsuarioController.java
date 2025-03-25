package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.services.UsuarioService;

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