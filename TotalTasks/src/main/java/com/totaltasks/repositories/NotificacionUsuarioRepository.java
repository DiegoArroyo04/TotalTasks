package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.totaltasks.entities.NotificacionUsuarioEntity;

import jakarta.transaction.Transactional;

public interface NotificacionUsuarioRepository extends JpaRepository<NotificacionUsuarioEntity, Long> {

    List<NotificacionUsuarioEntity> findByDestinatarioIdUsuarioAndVistaFalse(Long idUsuario);

    @Modifying
    @Transactional
    @Query("DELETE FROM NotificacionUsuarioEntity nu WHERE nu.notificacion.proyecto.id = :proyectoId")
    void deleteAllByProyectoId(@Param("proyectoId") Long proyectoId);
}
