package com.totaltasks.services.implementations;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.repositories.TablonRepository;
import com.totaltasks.services.ProyectoService;
import com.totaltasks.services.TablonService;

@Service
public class TablonServiceImplementation implements TablonService {

    @Autowired
    private TablonRepository tablonRepository;

    @Override
    public Long crearTablon(TablonDTO tablonDTO) {

        TablonEntity tablonEntity = new TablonEntity();
        tablonEntity.setNombreTablon(tablonDTO.getNombreTablon());
        tablonEntity.setOrden(tablonDTO.getOrden());
        tablonEntity.setProyecto(tablonDTO.getProyecto());
        tablonRepository.save(tablonEntity);

        return tablonEntity.getId();
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

    @Override
    public String eliminarTablon(ProyectoEntity proyecto, String nombreTablon) {

        // ELIMINAR LAS TAREAS DE ESE TABLON ASOCIADAS AL PROYECTO
        for (int i = 0; i < proyecto.getTareas().size(); i++) {
            if (proyecto.getTareas().get(i).getEstado().equals(nombreTablon)) {

            }
        }

        // ELIMINAR EL TABLON

        throw new UnsupportedOperationException("Unimplemented method 'eliminarTablon'");
    }

}