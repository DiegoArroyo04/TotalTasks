package com.totaltasks.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.totaltasks.entities.ProductBoardEntity;
import com.totaltasks.entities.ProyectoEntity;

import jakarta.transaction.Transactional;

@Repository
public interface ProductBoardRepository extends JpaRepository<ProductBoardEntity, Long> {
	
    List<ProductBoardEntity> findByProyecto_idProyecto(Long idProyecto);

	void deleteAllByProyecto(ProyectoEntity proyecto);

	List<ProductBoardEntity> findByEstadoNotAndFechaLimiteBefore(String estado, Timestamp fechaLimite);

	List<ProductBoardEntity> findByProyecto_idProyectoAndEstadoNotAndFechaLimiteBefore(
        Long idProyecto, String estado, Timestamp fechaLimite
    );

	@Query("SELECT t FROM ProductBoardEntity t WHERE t.idTareaBoard = :idTarea")
	ProductBoardEntity findTareaById(@Param("idTarea") Long idTarea);

	@Transactional
	void deleteByEstado(String estado);

	long countByEstado(String estado);

}