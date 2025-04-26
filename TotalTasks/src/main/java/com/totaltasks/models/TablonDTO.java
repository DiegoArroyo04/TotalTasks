package com.totaltasks.models;

import com.totaltasks.entities.ProyectoEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TablonDTO {

	private Long id;
	private String nombreTablon;
	private Integer orden;
	private ProyectoEntity proyecto;
	private Long id_proyecto;

	// CONSTRUCTOR PARA CREAR TABLONES POR DEFECTO
	public TablonDTO(String nombreTablon, Integer orden, ProyectoEntity proyecto) {
		this.nombreTablon = nombreTablon;
		this.orden = orden;
		this.proyecto = proyecto;
	}

	// CONSTRUCTOR PARA CREAR TABLON PERSONALIZADO
	public TablonDTO(String nombreTablon, Integer orden, Long id_proyecto) {
		this.nombreTablon = nombreTablon;
		this.orden = orden;
		this.id_proyecto = id_proyecto;
	}

	// CONSTRUCTOR PARA ELIMINAR TABLONES
	public TablonDTO(String nombreTablon, Long id_proyecto) {
		this.nombreTablon = nombreTablon;
		this.id_proyecto = id_proyecto;
	}

	@Override
	public String toString() {
		return "TablonDTO [id=" + id + ", nombreTablon=" + nombreTablon + ", orden=" + orden + ", proyecto=" + proyecto
				+ ", id_proyecto=" + id_proyecto + "]";
	}

}