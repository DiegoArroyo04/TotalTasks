package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;

public interface UsuarioService {
    String registrarUsuario(UsuarioDTO usuario);

    String registrarUsuarioGoogle(UsuarioDTO usuario);

    String registrarUsuarioGitHub(UsuarioDTO usuario);

    //Metodos
    String obtenerAccessTokenDeGitHub(String code);
    UsuarioDTO obtenerDatosUsuarioGitHub(String accessToken);

    List<RepoDTO> obtenerRepositoriosUsuarioGitHub(String accessToken);

    String comprobarLogin(UsuarioDTO usuario);

    // BUSCAR POR USUARIO
    UsuarioEntity encontrarUsuario(String usuario);
}