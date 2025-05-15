package com.totaltasks.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.totaltasks.models.ChatMessageDTO;
import com.totaltasks.services.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@GetMapping("/mensajes/{idProyecto}")
	public List<ChatMessageDTO> mensajes(@PathVariable Long idProyecto) {
		return chatService.obtenerMensajesPorProyecto(idProyecto);
	}
}