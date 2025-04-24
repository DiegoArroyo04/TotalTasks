package com.totaltasks.models;

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
    private Long idProyecto;
    
}