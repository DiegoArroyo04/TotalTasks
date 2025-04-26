package com.totaltasks.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TareaDTO {

	private Long idTarea;
	private String titulo;
	private String descripcion;
	private Timestamp fechaCreacion;
	private Date fechaLimite;
	private String estado;
	private Long idProyecto;
	private Long idResponsable;

	// CONSTRUCTOR PARA MODIFICAR EL ESTADO DE LAS TAREAS
	public TareaDTO(Long idTarea, String estado) {
		this.idTarea = idTarea;
		this.estado = estado;
	}

}