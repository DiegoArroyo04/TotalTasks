package com.totaltasks.services.implementations;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.TablonEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.repositories.TablonRepository;
import com.totaltasks.services.TablonService;

@Service
public class TablonServiceImplementation implements TablonService {

    @Autowired
    TablonRepository tablonRepository;

    @Override
    public String crearTablon(TablonDTO tablonDTO) {

        TablonEntity tablonEntity = new TablonEntity();
        tablonEntity.setNombreTablon(tablonDTO.getNombreTablon());
        tablonEntity.setOrden(tablonDTO.getOrden());
        tablonEntity.setProyecto(tablonDTO.getProyecto());
        tablonRepository.save(tablonEntity);

        return "Creado";
    }

    @Override
    public List<TablonEntity> ordenarTablones(List<TablonEntity> listaSinOrdenar) {
        listaSinOrdenar.sort(Comparator.comparing(TablonEntity::getOrden));
        return listaSinOrdenar;

    }

    @Override
    public String actualizarOrdenTablones(List<TablonDTO> tablonOrden) {
        for (TablonDTO dto : tablonOrden) {
            TablonEntity tablon = tablonRepository.findById(dto.getId()).orElse(null);
            if (tablon != null) {
                tablon.setOrden(dto.getOrden());
                tablonRepository.save(tablon);
            }
        }
        return "Orden actualizado";
    }

}