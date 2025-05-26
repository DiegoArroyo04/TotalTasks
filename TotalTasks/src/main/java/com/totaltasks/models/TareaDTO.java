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
	private Long idUsuario;

	// CONSTRUCTOR PARA MODIFICAR EL ESTADO DE LAS TAREAS
	public TareaDTO(Long idTarea, String estado) {
		this.idTarea = idTarea;
		this.estado = estado;
	}

	// CONSTRUCTOR PARA EDITAR LA TAREA
	public TareaDTO(Long idTarea, String titulo, String descripcion, Date fechaLimite, Long idProyecto,
			Long idResponsable) {
		this.idTarea = idTarea;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.fechaLimite = fechaLimite;
		this.idProyecto = idProyecto;
		this.idResponsable = idResponsable;
	}

}