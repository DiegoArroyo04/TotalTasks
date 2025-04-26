package com.totaltasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.totaltasks.entities.ProyectoEntity;

public interface ProyectoRepository extends JpaRepository<ProyectoEntity, Long> {

	@Query("SELECT DISTINCT p FROM ProyectoEntity p " +
			"LEFT JOIN p.usuarios up " +
			"WHERE p.creador.idUsuario = :idUsuario OR up.usuario.idUsuario = :idUsuario")
	List<ProyectoEntity> findTodosLosProyectosDeUnUsuario(@Param("idUsuario") Long idUsuario);

	ProyectoEntity findByCodigo(String codigo);

	List<ProyectoEntity> findByNombreProyecto(String nombreProyecto);

}