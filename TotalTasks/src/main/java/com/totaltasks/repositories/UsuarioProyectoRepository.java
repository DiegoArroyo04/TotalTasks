package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.UsuarioEntity;
import com.totaltasks.entities.UsuarioProyectoEntity;

public interface UsuarioProyectoRepository extends JpaRepository<UsuarioProyectoEntity, Long> {

    boolean existsByUsuarioAndProyecto(UsuarioEntity usuario, ProyectoEntity proyecto);

    UsuarioProyectoEntity findByUsuarioAndProyecto(UsuarioEntity usuario, ProyectoEntity proyecto);

    void deleteAllByProyecto(ProyectoEntity proyecto);

    List<UsuarioProyectoEntity> findByUsuario(UsuarioEntity usuario);
}