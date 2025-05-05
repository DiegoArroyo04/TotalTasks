package com.totaltasks.services;

import com.totaltasks.models.TareaDTO;

public interface TareaService {

	boolean crearTarea(TareaDTO dto);

	String modificarEstadoTarea(TareaDTO tareaDTO);

}