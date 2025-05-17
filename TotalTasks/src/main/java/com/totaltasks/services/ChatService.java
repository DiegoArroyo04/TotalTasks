package com.totaltasks.services;

import java.util.List;

import com.totaltasks.models.ChatMessageDTO;

public interface ChatService {
	ChatMessageDTO guardarMensaje(ChatMessageDTO dto);

	List<ChatMessageDTO> obtenerMensajesPorProyecto(Long idProyecto);
}