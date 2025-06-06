package com.totaltasks.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductBacklogDTO {

	private String titulo;
    private String descripcion;
    private Integer storyPoints;
    private String prioridad;
    private String estado;
    private Long idProyecto;
    private Long idCreador;

	// Constructor crear historia
	public ProductBacklogDTO(String titulo, String descripcion, Integer storyPoints, Long idProyecto, Long idUsuario) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.storyPoints = storyPoints;
		this.idProyecto = idProyecto;
		this.idCreador = idUsuario;
	}

}