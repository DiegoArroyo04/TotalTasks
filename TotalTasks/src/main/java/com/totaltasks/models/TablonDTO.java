package com.totaltasks.models;

import com.totaltasks.entities.ProyectoEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TablonDTO {

    private Long id;
    private String nombreTablon;
    private Integer orden;
    private ProyectoEntity proyecto;

    // CONSTRUCTOR PARA CREAR TABLONES POR DEFECTO
    public TablonDTO(String nombreTablon, Integer orden, ProyectoEntity proyecto) {
        this.nombreTablon = nombreTablon;
        this.orden = orden;
        this.proyecto = proyecto;
    }

}