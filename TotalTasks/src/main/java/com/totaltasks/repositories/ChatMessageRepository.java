package com.totaltasks.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.totaltasks.entities.ChatMessageEntity;
import com.totaltasks.entities.ProyectoEntity;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
	List<ChatMessageEntity> findByProyecto_IdProyectoOrderByFechaCreacionAsc(Long idProyecto);
	void deleteAllByProyecto(ProyectoEntity proyecto);
}