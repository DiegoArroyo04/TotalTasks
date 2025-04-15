package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.ProyectoEntity;

public interface ProyectoService {

    List<ProyectoEntity> todosLosProyectosDeUnUsuario(UsuarioEntity usuario);
    
}