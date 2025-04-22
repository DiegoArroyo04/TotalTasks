package com.totaltasks.services;

import java.util.List;

import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.entities.ProyectoEntity;

public interface ProyectoService {

    List<ProyectoEntity> todosLosProyectosDeUnUsuario(UsuarioEntity usuario);

    void guardarProyecto(ProyectoDTO proyecto);

    boolean unirseAProyectoPorCodigo(String codigo, UsuarioEntity usuario);

    ProyectoEntity obtenerProyectoPorNombre(String nombreProyecto);

    ProyectoEntity obtenerProyectoPorId(Long id);

    ProyectoEntity findByCodigo(String codigo);

    void deleteById(Long id, boolean abandonar);

    String obtenerCodigoAleatorio();

}