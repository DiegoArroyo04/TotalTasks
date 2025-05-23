package com.totaltasks.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.models.UsuarioDTO;

public interface UsuarioService {

	String registrarUsuario(UsuarioDTO usuario);

	String registrarUsuarioGoogle(UsuarioDTO usuario);

	String registrarUsuarioGitHub(UsuarioDTO usuario);

	// Metodos github
	String obtenerAccessTokenDeGitHub(String code);

	UsuarioDTO obtenerDatosUsuarioGitHub(String accessToken);

	// Metodo adicional datos Github
	List<RepoDTO> obtenerRepositoriosUsuarioGitHub(String accessToken);

	String comprobarLogin(UsuarioDTO usuario);

	// Buscar por usuario
	UsuarioEntity encontrarUsuario(String email);

	UsuarioEntity encontrarUsuarioPorUsuario(String usuario);

	void actualizarUsuario(String nombre, MultipartFile fotoPerfil, UsuarioEntity usuario) throws IOException;

	String convertirByteABase64(byte[] foto);

	UsuarioEntity obtenerUsuarioPorId(Long id);

}