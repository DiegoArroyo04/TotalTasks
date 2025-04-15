package com.totaltasks.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.services.ProyectoService;

@Service
public class ProyectoServiceImplementation implements ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Override
    public List<ProyectoEntity> todosLosProyectosDeUnUsuario(UsuarioEntity usuario) {
        return proyectoRepository.findTodosLosProyectosDeUnUsuario(usuario.getIdUsuario());
    }

    @Override
    public void guardarProyecto(ProyectoEntity proyecto) {
        proyectoRepository.save(proyecto);
    }

}