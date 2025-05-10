package com.totaltasks.services;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.TareaDTO;

public interface TareaService {

	boolean crearTarea(TareaDTO dto);

	String modificarEstadoTarea(TareaDTO tareaDTO, UsuarioEntity usuario);

}