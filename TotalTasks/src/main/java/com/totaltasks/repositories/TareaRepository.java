package com.totaltasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TareaEntity;

public interface TareaRepository extends JpaRepository<TareaEntity, Long> {

    void deleteAllByProyecto(ProyectoEntity proyecto);

}