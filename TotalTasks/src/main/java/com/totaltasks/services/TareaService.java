package com.totaltasks.services;

import java.sql.Date;
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

	void actualizarFechaTarea(Long idTarea, Date nuevaFecha, Long idUsuario);

	void actualizarTarea(TareaDTO tarea);

	void deleteById(Long id, Long idUsuario, Long idProyecto);

}