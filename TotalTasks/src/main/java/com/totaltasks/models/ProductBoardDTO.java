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
public class ProductBoardDTO {
    private Long idTareaBoard;
    private String titulo;
    private String descripcion;
    private String estado;
    private Long idProyecto;
    private Long idResponsable;
}