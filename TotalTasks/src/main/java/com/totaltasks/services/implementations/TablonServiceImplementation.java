package com.totaltasks.services.implementations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;
import com.totaltasks.entities.TareaEntity;
import com.totaltasks.models.TablonDTO;
import com.totaltasks.repositories.TablonRepository;
import com.totaltasks.repositories.TareaRepository;
import com.totaltasks.services.TablonService;

import jakarta.transaction.Transactional;

@Service
public class TablonServiceImplementation implements TablonService {

    @Autowired
    private TablonRepository tablonRepository;

    @Autowired
    private TareaRepository tareaRepository;

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

    @Transactional
    @Override
    public String eliminarTablon(ProyectoEntity proyecto, String nombreTablon) {
    
        // Primero: encontrar el tablon
        TablonEntity tablon = tablonRepository.findByNombreTablonAndProyecto(nombreTablon, proyecto);
    
        if (tablon == null) {
            return "Tablón no encontrado";
        }
    
        // Comprobamos que sigue existiendo en base de datos (por si acaso)
        Optional<TablonEntity> tablonOpt = tablonRepository.findById(tablon.getId());
        if (tablonOpt.isEmpty()) {
            return "El tablón ya no existe";
        }
    
        // Segundo: recoger todas las tareas que estén en ese tablero
        List<TareaEntity> tareasAEliminar = new ArrayList<>();
        for (TareaEntity tarea : proyecto.getTareas()) {
            if (tarea.getEstado().equals(nombreTablon)) {
                tareasAEliminar.add(tarea);
            }
        }
    
        // Tercero: eliminar esas tareas de la base de datos
        tareaRepository.deleteAll(tareasAEliminar);
        proyecto.getTareas().removeAll(tareasAEliminar); // opcional, en memoria
    
        // Cuarto: eliminar el tablon
        tablonRepository.delete(tablonOpt.get());
        tablonRepository.flush(); // sincronizamos ahora con la base de datos
    
        return "Tablón eliminado exitosamente";
    }    

}