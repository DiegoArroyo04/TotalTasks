package com.totaltasks.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.UsuarioProyectoEntity;
import com.totaltasks.models.ProyectoDTO;
import com.totaltasks.repositories.ProyectoRepository;
import com.totaltasks.repositories.UsuarioProyectoRepository;
import com.totaltasks.repositories.UsuarioRepository;
import com.totaltasks.services.ProyectoService;

@Service
public class ProyectoServiceImplementation implements ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioProyectoRepository usuarioProyectoRepository;

    @Override
    public List<ProyectoEntity> todosLosProyectosDeUnUsuario(UsuarioEntity usuario) {
        return proyectoRepository.findTodosLosProyectosDeUnUsuario(usuario.getIdUsuario());
    }

    @Override
    public void guardarProyecto(ProyectoDTO proyectoDTO) {

        // Guardamos el proyecto
        ProyectoEntity proyectoEntity = new ProyectoEntity();

        proyectoEntity.setNombreProyecto(proyectoDTO.getNombreProyecto());
        proyectoEntity.setDescripcion(proyectoDTO.getDescripcion());
        proyectoEntity.setMetodologia(proyectoDTO.getMetodologia());

        UsuarioEntity creador = usuarioRepository.findById(proyectoDTO.getIdCreador()).orElse(null);
        proyectoEntity.setCreador(creador);

        ProyectoEntity proyectoGuardado = proyectoRepository.save(proyectoEntity);

        // Guardamos la relacion entre el Usuario y el Proyecto
        UsuarioProyectoEntity usuarioProyecto = new UsuarioProyectoEntity();

        usuarioProyecto.setUsuario(creador);
        usuarioProyecto.setProyecto(proyectoGuardado);
        usuarioProyecto.setRol("Administrador");

        usuarioProyectoRepository.save(usuarioProyecto);
    }

    @Override
    public boolean unirseAProyectoPorCodigo(String codigo, UsuarioEntity usuario) {
        ProyectoEntity proyecto = proyectoRepository.findByCodigo(codigo);

        if (proyecto == null) {
            return false;
        }

        // Comprobar si ya está unido
        boolean yaUnido = usuarioProyectoRepository.existsByUsuarioAndProyecto(usuario, proyecto);
        if (yaUnido) {
            return false;
        }

        UsuarioProyectoEntity usuarioProyecto = new UsuarioProyectoEntity();
        usuarioProyecto.setUsuario(usuario);
        usuarioProyecto.setProyecto(proyecto);
        usuarioProyecto.setRol("Colaborador");

        usuarioProyectoRepository.save(usuarioProyecto);
        return true;
    }

    @Override
    public ProyectoEntity obtenerProyectoPorNombre(String nombreProyecto) {
        List<ProyectoEntity> proyectos = proyectoRepository.findByNombreProyecto(nombreProyecto);
        return proyectos.isEmpty() ? null : proyectos.get(0);
    }

}