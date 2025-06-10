package com.totaltasks.repositories;

import com.totaltasks.entities.ProyectoEntity;
import com.totaltasks.entities.SprintEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {

	List<SprintEntity> findAllByProyecto_idProyecto(Long idProyecto);

	List<SprintEntity> findByProyecto_idProyecto(Long idProyecto);
	
	void deleteAllByProyecto(ProyectoEntity proyecto);

	@Query("SELECT s FROM SprintEntity s WHERE s.proyecto.idProyecto = :idProyecto AND s.fechaFin > CURRENT_TIMESTAMP ORDER BY s.fechaFin ASC")
	List<SprintEntity> findHistoriasSprintActivas(@Param("idProyecto") Long idProyecto);

}