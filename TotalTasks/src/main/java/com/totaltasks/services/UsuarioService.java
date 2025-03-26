package com.totaltasks.services;

import java.io.IOException;
import java.util.List;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;

public interface UsuarioService {
    String registrarUsuario(UsuarioDTO usuario);

    String registrarUsuarioGoogle(UsuarioDTO usuario);

    String registrarUsuarioGitHub(UsuarioDTO usuario);

    //Metodos github
    String obtenerAccessTokenDeGitHub(String code);
    UsuarioDTO obtenerDatosUsuarioGitHub(String accessToken);

    //Metodo adicional datos Github
    List<RepoDTO> obtenerRepositoriosUsuarioGitHub(String accessToken);

    String comprobarLogin(UsuarioDTO usuario);

    // BUSCAR POR USUARIO
    UsuarioEntity encontrarUsuario(String usuario);
}