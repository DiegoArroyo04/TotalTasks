package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.TareaEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.TareaDTO;

public interface TareaService {

	boolean crearTarea(TareaDTO dto);

	String modificarEstadoTarea(TareaDTO tareaDTO, UsuarioEntity usuario);

	void verificarTareasProximas(UsuarioEntity usuario);

	void crearRecordatorioFecha(TareaEntity tarea);

	List<TareaEntity> obtenerTareasPorUserYProyecto(Long usuarioId, Long proyectoId);

}