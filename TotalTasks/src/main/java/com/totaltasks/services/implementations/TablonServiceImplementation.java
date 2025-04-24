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
    public void crearTablon(TablonDTO tablonDTO) {

        TablonEntity tablonEntity = new TablonEntity();
        tablonEntity.setNombreTablon(tablonDTO.getNombreTablon());
        tablonEntity.setOrden(tablonDTO.getOrden());
        tablonEntity.setProyecto(tablonDTO.getProyecto());
        tablonRepository.save(tablonEntity);

    }

    @Override
    public List<TablonEntity> ordenarTablones(List<TablonEntity> listaSinOrdenar) {
        listaSinOrdenar.sort(Comparator.comparing(TablonEntity::getOrden));
        return listaSinOrdenar;

    }

}
