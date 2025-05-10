package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.totaltasks.entities.ProductBacklogEntity;

public interface ScrumRepository extends JpaRepository<ProductBacklogEntity, Long> {
	List<ProductBacklogEntity> findByProyecto_idProyecto(Long idProyecto);
}