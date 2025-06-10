package com.totaltasks.models;

import java.sql.Timestamp;

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
public class SprintDTO {

    private Long idSprint;
    private String titulo;
    private String descripcion;
	private Integer storyPoints;
    private String prioridad;
    private String estado;
	private Timestamp fechaFin;
    private Long idProyecto;
    private Long idResponsable;

}
