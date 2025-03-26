package com.totaltasks.services;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.UsuarioDTO;

public interface UsuarioService {
    String registrarUsuario(UsuarioDTO usuario);

    String registrarUsuarioGoogle(UsuarioDTO usuario);

    String comprobarLogin(UsuarioDTO usuario);

    // BUSCAR POR USUARIO
    UsuarioEntity encontrarUsuario(String usuario);
}