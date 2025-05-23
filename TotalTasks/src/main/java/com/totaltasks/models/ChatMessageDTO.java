package com.totaltasks.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
	private Long id_message;
	private String contenido;
	private Timestamp fechaCreacion;
	private Long idProyecto;
	private Long idUsuario;
	private String nombreUsuario;
}