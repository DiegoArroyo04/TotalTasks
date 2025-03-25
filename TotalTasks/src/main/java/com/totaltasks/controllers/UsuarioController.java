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