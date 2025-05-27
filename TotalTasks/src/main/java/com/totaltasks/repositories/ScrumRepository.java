package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.ProductBacklogEntity;
import com.totaltasks.entities.ProyectoEntity;

public interface ScrumRepository extends JpaRepository<ProductBacklogEntity, Long> {
	
	List<ProductBacklogEntity> findByProyecto_idProyecto(Long idProyecto);

	void deleteAllByProyecto(ProyectoEntity proyecto);
}