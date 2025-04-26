package com.totaltasks.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {

	private Long idProyecto;
	private String nombreProyecto;
	private String descripcion;
	private Timestamp fechaCreacion;
	private String metodologia;
	private String codigo;
	private Long idCreador;
	private String nombreCreador;

}