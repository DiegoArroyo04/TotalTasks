package com.totaltasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.NotificacionEntity;
import com.totaltasks.entities.ProyectoEntity;

public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Long> {
    void deleteAllByProyecto(ProyectoEntity proyecto);
}
