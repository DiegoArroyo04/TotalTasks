package com.totaltasks.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.totaltasks.entities.ChatMessageEntity;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
	List<ChatMessageEntity> findByProyecto_IdProyectoOrderByFechaCreacionAsc(Long idProyecto);
}