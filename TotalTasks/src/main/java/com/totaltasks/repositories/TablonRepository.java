package com.totaltasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.TablonEntity;

public interface TablonRepository extends JpaRepository<TablonEntity, Long> {

	void deleteAllByProyecto(ProyectoEntity proyecto);

	TablonEntity findByNombreTablonAndProyecto(String nombreTablon, ProyectoEntity proyecto);

	boolean existsByNombreTablonIgnoreCaseAndIdNotAndProyecto_IdProyecto(String nombreTablon, Long idNot, Long proyectoId);
}
