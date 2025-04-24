package com.totaltasks.services;

import java.util.List;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.models.TablonDTO;

public interface TablonService {

    String crearTablon(TablonDTO tablonDTO);

    List<TablonEntity> ordenarTablones(List<TablonEntity> listaSinOrdenar);

    String actualizarOrdenTablones(List<TablonDTO> tablonOrdenList);

}