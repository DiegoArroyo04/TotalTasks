package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.models.TablonDTO;

public interface TablonService {

	Long crearTablon(TablonDTO tablonDTO);

	List<TablonEntity> ordenarTablones(List<TablonEntity> listaSinOrdenar);

	String actualizarOrdenTablones(List<TablonDTO> tablonOrdenList);

	String eliminarTablon(ProyectoEntity proyecto, String nombreTablon);

}