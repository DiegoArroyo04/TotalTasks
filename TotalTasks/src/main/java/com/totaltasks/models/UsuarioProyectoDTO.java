package com.totaltasks.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioProyectoDTO {

	private Long idUsuario;
	private Long idProyecto;
	private String rol;
	private String color;
	private String colorHover;
	private String customHover;

	// CONSTRUCTOR PARA MODIFICAR EL COLOR DE UN PROYECTO A UN USUARIO
	public UsuarioProyectoDTO(Long idUsuario, Long idProyecto, String color, String colorHover) {
		this.idUsuario = idUsuario;
		this.idProyecto = idProyecto;
		this.color = color;
		this.colorHover = colorHover;
	}

}