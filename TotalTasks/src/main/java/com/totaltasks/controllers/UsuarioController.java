package com.totaltasks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totaltasks.models.UsuarioDTO;
import com.totaltasks.services.UsuarioService;

@RestController
@RequestMapping("/usuarios/")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("registrar")
    public String registrarUsuario(@RequestBody UsuarioDTO usuario) {

        return usuarioService.registrarUsuario(usuario);
    }

}