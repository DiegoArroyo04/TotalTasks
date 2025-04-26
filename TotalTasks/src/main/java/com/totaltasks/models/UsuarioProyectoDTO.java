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

}