package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.models.UsuarioProyectoDTO;

public interface TablonService {

	Long crearTablon(TablonDTO tablonDTO, UsuarioEntity usuario);

	List<TablonEntity> ordenarTablones(List<TablonEntity> listaSinOrdenar);

	String actualizarOrdenTablones(List<TablonDTO> tablonOrdenList, UsuarioEntity usuario);

	String eliminarTablon(ProyectoEntity proyecto, String nombreTablon, UsuarioEntity usuario);

	void guardarColores(UsuarioProyectoDTO usuarioProyectoDTO);

	UsuarioProyectoDTO obtenerColores(Long usuarioId, Long proyectoId);

	boolean actualizarNombreTablon(Long idTablon, String nuevoNombre, UsuarioEntity usuario);
}