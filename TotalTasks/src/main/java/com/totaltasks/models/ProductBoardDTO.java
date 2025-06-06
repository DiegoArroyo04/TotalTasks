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
public class ProductBoardDTO {

    private Long idTareaBoard;
    private String titulo;
    private String descripcion;
	private Integer storyPoints;
    private String prioridad;
    private String estado;
	private Timestamp fechaLimite;
    private Long idProyecto;
    private Long idResponsable;
	
}