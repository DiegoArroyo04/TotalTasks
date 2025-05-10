package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.models.RepoDTO;
import com.totaltasks.entities.ProyectoEntity;

public interface ProyectoService {

	List<ProyectoEntity> todosLosProyectosDeUnUsuario(UsuarioEntity usuario);

	void guardarProyecto(ProyectoDTO proyecto);

	boolean unirseAProyectoPorCodigo(String codigo, UsuarioEntity usuario);

	ProyectoEntity obtenerProyectoPorNombreYUsuario(String nombreProyecto, UsuarioEntity usuario);

	ProyectoEntity obtenerProyectoPorId(Long id);

	ProyectoEntity findByCodigo(String codigo);

	void deleteById(Long id, boolean abandonar);

	String obtenerCodigoAleatorio();

	RepoDTO comprobarRepo(ProyectoEntity proyecto, List<RepoDTO> repositorios);

	boolean usuarioExiste(ProyectoEntity proyecto, UsuarioEntity usuario);

}