package com.totaltasks.repositories;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.SprintEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {

	List<SprintEntity> findAllByProyecto_idProyecto(Long idProyecto);

	List<SprintEntity> findByProyecto_idProyecto(Long idProyecto);
	
	void deleteAllByProyecto(ProyectoEntity proyecto);
}