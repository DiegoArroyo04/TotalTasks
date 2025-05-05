package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.NotificacionUsuarioEntity;

public interface NotificacionUsuarioRepository extends JpaRepository<NotificacionUsuarioEntity, Long> {

    List<NotificacionUsuarioEntity> findByDestinatarioIdUsuarioAndVistaFalse(Long idUsuario);
}
